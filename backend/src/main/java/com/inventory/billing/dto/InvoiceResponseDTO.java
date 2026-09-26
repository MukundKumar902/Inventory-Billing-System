package com.inventory.billing.dto;

import java.time.LocalDateTime;
import java.util.List;

public class InvoiceResponseDTO {
    private Long id;
    private String invoiceNumber;
    private LocalDateTime invoiceDate;
    private Double subTotal;
    private Double totalDiscount;
    private Double totalTax;
    private Double grandTotal;
    private String paymentMode;
    private String customerName;
    private String customerPhone;
    private String cashierName;
    private List<ItemDetails> items;

    public InvoiceResponseDTO() {}

    public InvoiceResponseDTO(Long id, String invoiceNumber, LocalDateTime invoiceDate, Double subTotal, Double totalDiscount, Double totalTax, Double grandTotal, String paymentMode, String customerName, String customerPhone, String cashierName, List<ItemDetails> items) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.subTotal = subTotal;
        this.totalDiscount = totalDiscount;
        this.totalTax = totalTax;
        this.grandTotal = grandTotal;
        this.paymentMode = paymentMode;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.cashierName = cashierName;
        this.items = items;
    }

    public static InvoiceResponseDTOBuilder builder() {
        return new InvoiceResponseDTOBuilder();
    }

    public static class InvoiceResponseDTOBuilder {
        private Long id;
        private String invoiceNumber;
        private LocalDateTime invoiceDate;
        private Double subTotal;
        private Double totalDiscount;
        private Double totalTax;
        private Double grandTotal;
        private String paymentMode;
        private String customerName;
        private String customerPhone;
        private String cashierName;
        private List<ItemDetails> items;

        public InvoiceResponseDTOBuilder id(Long id) { this.id = id; return this; }
        public InvoiceResponseDTOBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public InvoiceResponseDTOBuilder invoiceDate(LocalDateTime invoiceDate) { this.invoiceDate = invoiceDate; return this; }
        public InvoiceResponseDTOBuilder subTotal(Double subTotal) { this.subTotal = subTotal; return this; }
        public InvoiceResponseDTOBuilder totalDiscount(Double totalDiscount) { this.totalDiscount = totalDiscount; return this; }
        public InvoiceResponseDTOBuilder totalTax(Double totalTax) { this.totalTax = totalTax; return this; }
        public InvoiceResponseDTOBuilder grandTotal(Double grandTotal) { this.grandTotal = grandTotal; return this; }
        public InvoiceResponseDTOBuilder paymentMode(String paymentMode) { this.paymentMode = paymentMode; return this; }
        public InvoiceResponseDTOBuilder customerName(String customerName) { this.customerName = customerName; return this; }
        public InvoiceResponseDTOBuilder customerPhone(String customerPhone) { this.customerPhone = customerPhone; return this; }
        public InvoiceResponseDTOBuilder cashierName(String cashierName) { this.cashierName = cashierName; return this; }
        public InvoiceResponseDTOBuilder items(List<ItemDetails> items) { this.items = items; return this; }
        public InvoiceResponseDTO build() { return new InvoiceResponseDTO(id, invoiceNumber, invoiceDate, subTotal, totalDiscount, totalTax, grandTotal, paymentMode, customerName, customerPhone, cashierName, items); }
    }

    public static class ItemDetails {
        private String productName;
        private Integer quantity;
        private Double unitPrice;
        private Double gstPercent;
        private Double gstAmount;
        private Double totalPrice;

        public ItemDetails() {}

        public ItemDetails(String productName, Integer quantity, Double unitPrice, Double gstPercent, Double gstAmount, Double totalPrice) {
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.gstPercent = gstPercent;
            this.gstAmount = gstAmount;
            this.totalPrice = totalPrice;
        }

        public static ItemDetailsBuilder builder() {
            return new ItemDetailsBuilder();
        }

        public static class ItemDetailsBuilder {
            private String productName;
            private Integer quantity;
            private Double unitPrice;
            private Double gstPercent;
            private Double gstAmount;
            private Double totalPrice;

            public ItemDetailsBuilder productName(String productName) { this.productName = productName; return this; }
            public ItemDetailsBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
            public ItemDetailsBuilder unitPrice(Double unitPrice) { this.unitPrice = unitPrice; return this; }
            public ItemDetailsBuilder gstPercent(Double gstPercent) { this.gstPercent = gstPercent; return this; }
            public ItemDetailsBuilder gstAmount(Double gstAmount) { this.gstAmount = gstAmount; return this; }
            public ItemDetailsBuilder totalPrice(Double totalPrice) { this.totalPrice = totalPrice; return this; }
            public ItemDetails build() { return new ItemDetails(productName, quantity, unitPrice, gstPercent, gstAmount, totalPrice); }
        }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public Double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }

        public Double getGstPercent() { return gstPercent; }
        public void setGstPercent(Double gstPercent) { this.gstPercent = gstPercent; }

        public Double getGstAmount() { return gstAmount; }
        public void setGstAmount(Double gstAmount) { this.gstAmount = gstAmount; }

        public Double getTotalPrice() { return totalPrice; }
        public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public LocalDateTime getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDateTime invoiceDate) { this.invoiceDate = invoiceDate; }

    public Double getSubTotal() { return subTotal; }
    public void setSubTotal(Double subTotal) { this.subTotal = subTotal; }

    public Double getTotalDiscount() { return totalDiscount; }
    public void setTotalDiscount(Double totalDiscount) { this.totalDiscount = totalDiscount; }

    public Double getTotalTax() { return totalTax; }
    public void setTotalTax(Double totalTax) { this.totalTax = totalTax; }

    public Double getGrandTotal() { return grandTotal; }
    public void setGrandTotal(Double grandTotal) { this.grandTotal = grandTotal; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCashierName() { return cashierName; }
    public void setCashierName(String cashierName) { this.cashierName = cashierName; }

    public List<ItemDetails> getItems() { return items; }
    public void setItems(List<ItemDetails> items) { this.items = items; }
}
