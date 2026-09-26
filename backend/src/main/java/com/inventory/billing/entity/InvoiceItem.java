package com.inventory.billing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @Column(name = "gst_percent")
    private Double gstPercent = 0.0;

    @Column(name = "gst_amount")
    private Double gstAmount = 0.0;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    public InvoiceItem() {}

    public InvoiceItem(Long id, Invoice invoice, Product product, Integer quantity, Double unitPrice, Double gstPercent, Double gstAmount, Double totalPrice) {
        this.id = id;
        this.invoice = invoice;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.gstPercent = gstPercent != null ? gstPercent : 0.0;
        this.gstAmount = gstAmount != null ? gstAmount : 0.0;
        this.totalPrice = totalPrice;
    }

    public static InvoiceItemBuilder builder() {
        return new InvoiceItemBuilder();
    }

    public static class InvoiceItemBuilder {
        private Long id;
        private Invoice invoice;
        private Product product;
        private Integer quantity;
        private Double unitPrice;
        private Double gstPercent = 0.0;
        private Double gstAmount = 0.0;
        private Double totalPrice;

        public InvoiceItemBuilder id(Long id) { this.id = id; return this; }
        public InvoiceItemBuilder invoice(Invoice invoice) { this.invoice = invoice; return this; }
        public InvoiceItemBuilder product(Product product) { this.product = product; return this; }
        public InvoiceItemBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public InvoiceItemBuilder unitPrice(Double unitPrice) { this.unitPrice = unitPrice; return this; }
        public InvoiceItemBuilder gstPercent(Double gstPercent) { this.gstPercent = gstPercent; return this; }
        public InvoiceItemBuilder gstAmount(Double gstAmount) { this.gstAmount = gstAmount; return this; }
        public InvoiceItemBuilder totalPrice(Double totalPrice) { this.totalPrice = totalPrice; return this; }
        public InvoiceItem build() { return new InvoiceItem(id, invoice, product, quantity, unitPrice, gstPercent, gstAmount, totalPrice); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

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
