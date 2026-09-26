package com.inventory.billing.service;

import com.inventory.billing.dto.SalesReportDTO;

public interface ReportService {
    SalesReportDTO getTodaySalesReport();
    SalesReportDTO getMonthlySalesReport();
}
