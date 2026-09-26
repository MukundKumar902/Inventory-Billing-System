package com.inventory.billing.controller;

import com.inventory.billing.dto.InvoiceRequestDTO;
import com.inventory.billing.dto.InvoiceResponseDTO;
import com.inventory.billing.service.BillingService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@Valid @RequestBody InvoiceRequestDTO request, Principal principal) {
        String username = (principal != null) ? principal.getName() : "admin";
        InvoiceResponseDTO invoice = billingService.createInvoice(request, username);
        return new ResponseEntity<>(invoice, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponseDTO>> getAllInvoices() {
        return ResponseEntity.ok(billingService.getAllInvoices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.getInvoiceById(id));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<InputStreamResource> downloadInvoicePdf(@PathVariable Long id) {
        ByteArrayInputStream pdfStream = billingService.getInvoicePdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice-" + id + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }
}
