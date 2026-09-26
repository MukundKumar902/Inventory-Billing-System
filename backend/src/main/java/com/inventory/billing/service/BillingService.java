package com.inventory.billing.service;

import com.inventory.billing.dto.InvoiceRequestDTO;
import com.inventory.billing.dto.InvoiceResponseDTO;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface BillingService {
    InvoiceResponseDTO createInvoice(InvoiceRequestDTO request, String username);
    InvoiceResponseDTO getInvoiceById(Long id);
    List<InvoiceResponseDTO> getAllInvoices();
    ByteArrayInputStream getInvoicePdf(Long invoiceId);
}
