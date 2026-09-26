package com.inventory.billing.service;

import com.inventory.billing.dto.SalesReportDTO;
import com.inventory.billing.repository.InvoiceRepository;
import com.inventory.billing.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ReportServiceImpl implements ReportService {

    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;

    public ReportServiceImpl(InvoiceRepository invoiceRepository, ProductRepository productRepository) {
        this.invoiceRepository = invoiceRepository;
        this.productRepository = productRepository;
    }

    @Override
    public SalesReportDTO getTodaySalesReport() {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        Double revenue = invoiceRepository.calculateTotalRevenueBetween(startOfDay, endOfDay);
        Long invoiceCount = invoiceRepository.countInvoicesBetween(startOfDay, endOfDay);
        Long totalProducts = productRepository.count();
        int lowStockCount = productRepository.findLowStockProducts().size();

        return SalesReportDTO.builder()
                .totalRevenue(revenue != null ? revenue : 0.0)
                .totalInvoices(invoiceCount != null ? invoiceCount : 0L)
                .totalProductsCount(totalProducts)
                .lowStockProductsCount(lowStockCount)
                .build();
    }

    @Override
    public SalesReportDTO getMonthlySalesReport() {
        LocalDateTime startOfMonth = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        LocalDateTime endOfMonth = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        Double revenue = invoiceRepository.calculateTotalRevenueBetween(startOfMonth, endOfMonth);
        Long invoiceCount = invoiceRepository.countInvoicesBetween(startOfMonth, endOfMonth);
        Long totalProducts = productRepository.count();
        int lowStockCount = productRepository.findLowStockProducts().size();

        return SalesReportDTO.builder()
                .totalRevenue(revenue != null ? revenue : 0.0)
                .totalInvoices(invoiceCount != null ? invoiceCount : 0L)
                .totalProductsCount(totalProducts)
                .lowStockProductsCount(lowStockCount)
                .build();
    }
}
