package com.inventory.billing.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class InvoiceRequestDTO {
    private String customerName;
    private String customerPhone;
    private String paymentMode = "CASH";

    @NotEmpty(message = "Cart cannot be empty")
    private List<InvoiceItemRequestDTO> items;

    public InvoiceRequestDTO() {}

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public List<InvoiceItemRequestDTO> getItems() { return items; }
    public void setItems(List<InvoiceItemRequestDTO> items) { this.items = items; }
}
