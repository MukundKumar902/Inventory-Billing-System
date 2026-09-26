package com.inventory.billing.dto;

public class SalesReportDTO {
    private Double totalRevenue;
    private Long totalInvoices;
    private Long totalProductsCount;
    private Integer lowStockProductsCount;

    public SalesReportDTO() {}

    public SalesReportDTO(Double totalRevenue, Long totalInvoices, Long totalProductsCount, Integer lowStockProductsCount) {
        this.totalRevenue = totalRevenue;
        this.totalInvoices = totalInvoices;
        this.totalProductsCount = totalProductsCount;
        this.lowStockProductsCount = lowStockProductsCount;
    }

    public static SalesReportDTOBuilder builder() {
        return new SalesReportDTOBuilder();
    }

    public static class SalesReportDTOBuilder {
        private Double totalRevenue;
        private Long totalInvoices;
        private Long totalProductsCount;
        private Integer lowStockProductsCount;

        public SalesReportDTOBuilder totalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; return this; }
        public SalesReportDTOBuilder totalInvoices(Long totalInvoices) { this.totalInvoices = totalInvoices; return this; }
        public SalesReportDTOBuilder totalProductsCount(Long totalProductsCount) { this.totalProductsCount = totalProductsCount; return this; }
        public SalesReportDTOBuilder lowStockProductsCount(Integer lowStockProductsCount) { this.lowStockProductsCount = lowStockProductsCount; return this; }
        public SalesReportDTO build() { return new SalesReportDTO(totalRevenue, totalInvoices, totalProductsCount, lowStockProductsCount); }
    }

    public Double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }

    public Long getTotalInvoices() { return totalInvoices; }
    public void setTotalInvoices(Long totalInvoices) { this.totalInvoices = totalInvoices; }

    public Long getTotalProductsCount() { return totalProductsCount; }
    public void setTotalProductsCount(Long totalProductsCount) { this.totalProductsCount = totalProductsCount; }

    public Integer getLowStockProductsCount() { return lowStockProductsCount; }
    public void setLowStockProductsCount(Integer lowStockProductsCount) { this.lowStockProductsCount = lowStockProductsCount; }
}
