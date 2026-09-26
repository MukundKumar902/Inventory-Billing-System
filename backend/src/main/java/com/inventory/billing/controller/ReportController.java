package com.inventory.billing.controller;

import com.inventory.billing.dto.SalesReportDTO;
import com.inventory.billing.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/today")
    public ResponseEntity<SalesReportDTO> getTodaySalesReport() {
        return ResponseEntity.ok(reportService.getTodaySalesReport());
    }

    @GetMapping("/monthly")
    public ResponseEntity<SalesReportDTO> getMonthlySalesReport() {
        return ResponseEntity.ok(reportService.getMonthlySalesReport());
    }
}
