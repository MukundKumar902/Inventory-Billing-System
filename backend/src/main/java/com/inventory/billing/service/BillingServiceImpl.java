package com.inventory.billing.service;

import com.inventory.billing.dto.InvoiceItemRequestDTO;
import com.inventory.billing.dto.InvoiceRequestDTO;
import com.inventory.billing.dto.InvoiceResponseDTO;
import com.inventory.billing.entity.*;
import com.inventory.billing.exception.InsufficientStockException;
import com.inventory.billing.exception.ResourceNotFoundException;
import com.inventory.billing.repository.CustomerRepository;
import com.inventory.billing.repository.InvoiceRepository;
import com.inventory.billing.repository.ProductRepository;
import com.inventory.billing.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingServiceImpl implements BillingService {

    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PdfGeneratorService pdfGeneratorService;

    public BillingServiceImpl(InvoiceRepository invoiceRepository, ProductRepository productRepository, UserRepository userRepository, CustomerRepository customerRepository, PdfGeneratorService pdfGeneratorService) {
        this.invoiceRepository = invoiceRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @Override
    @Transactional
    public InvoiceResponseDTO createInvoice(InvoiceRequestDTO request, String username) {
        User cashier = userRepository.findByUsername(username).orElse(null);

        Customer customer = null;
        if (request.getCustomerPhone() != null && !request.getCustomerPhone().isBlank()) {
            customer = customerRepository.findByPhone(request.getCustomerPhone())
                    .orElseGet(() -> customerRepository.save(
                            Customer.builder()
                                    .name(request.getCustomerName() != null ? request.getCustomerName() : "Valued Customer")
                                    .phone(request.getCustomerPhone())
                                    .build()
                    ));
        }

        Invoice invoice = Invoice.builder()
                .invoiceNumber("INV-" + System.currentTimeMillis())
                .invoiceDate(LocalDateTime.now())
                .paymentMode(request.getPaymentMode() != null ? request.getPaymentMode() : "CASH")
                .createdBy(cashier)
                .customer(customer)
                .items(new ArrayList<>())
                .build();

        double subTotal = 0.0;
        double totalTax = 0.0;

        for (InvoiceItemRequestDTO itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemReq.getProductId()));

            if (product.getCurrentStock() < itemReq.getQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product '" + product.getName() + 
                        "'. Available: " + product.getCurrentStock() + ", Requested: " + itemReq.getQuantity()
                );
            }

            product.setCurrentStock(product.getCurrentStock() - itemReq.getQuantity());
            productRepository.save(product);

            double unitPrice = product.getPrice();
            double gstPercent = product.getGstPercent() != null ? product.getGstPercent() : 0.0;
            double baseAmount = unitPrice * itemReq.getQuantity();
            double gstAmount = (baseAmount * gstPercent) / 100.0;
            double itemTotal = baseAmount + gstAmount;

            subTotal += baseAmount;
            totalTax += gstAmount;

            InvoiceItem invoiceItem = InvoiceItem.builder()
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(unitPrice)
                    .gstPercent(gstPercent)
                    .gstAmount(gstAmount)
                    .totalPrice(itemTotal)
                    .build();

            invoice.addItem(invoiceItem);
        }

        invoice.setSubTotal(subTotal);
        invoice.setTotalTax(totalTax);
        invoice.setTotalDiscount(0.0);
        invoice.setGrandTotal(subTotal + totalTax);

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return mapToDTO(savedInvoice);
    }

    @Override
    public InvoiceResponseDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        return mapToDTO(invoice);
    }

    @Override
    public List<InvoiceResponseDTO> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ByteArrayInputStream getInvoicePdf(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + invoiceId));
        return pdfGeneratorService.generateInvoicePdf(invoice);
    }

    private InvoiceResponseDTO mapToDTO(Invoice invoice) {
        List<InvoiceResponseDTO.ItemDetails> itemDetails = invoice.getItems().stream()
                .map(item -> InvoiceResponseDTO.ItemDetails.builder()
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .gstPercent(item.getGstPercent())
                        .gstAmount(item.getGstAmount())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return InvoiceResponseDTO.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .invoiceDate(invoice.getInvoiceDate())
                .subTotal(invoice.getSubTotal())
                .totalDiscount(invoice.getTotalDiscount())
                .totalTax(invoice.getTotalTax())
                .grandTotal(invoice.getGrandTotal())
                .paymentMode(invoice.getPaymentMode())
                .customerName(invoice.getCustomer() != null ? invoice.getCustomer().getName() : "Walk-in")
                .customerPhone(invoice.getCustomer() != null ? invoice.getCustomer().getPhone() : "-")
                .cashierName(invoice.getCreatedBy() != null ? invoice.getCreatedBy().getFullName() : "Admin")
                .items(itemDetails)
                .build();
    }
}
