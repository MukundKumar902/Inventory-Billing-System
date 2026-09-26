package com.inventory.billing.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;

    @Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate;

    @Column(name = "sub_total", nullable = false)
    private Double subTotal;

    @Column(name = "total_discount")
    private Double totalDiscount = 0.0;

    @Column(name = "total_tax")
    private Double totalTax = 0.0;

    @Column(name = "grand_total", nullable = false)
    private Double grandTotal;

    @Column(name = "payment_mode")
    private String paymentMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> items = new ArrayList<>();

    public Invoice() {}

    public Invoice(Long id, String invoiceNumber, LocalDateTime invoiceDate, Double subTotal, Double totalDiscount, Double totalTax, Double grandTotal, String paymentMode, User createdBy, Customer customer, List<InvoiceItem> items) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.subTotal = subTotal;
        this.totalDiscount = totalDiscount != null ? totalDiscount : 0.0;
        this.totalTax = totalTax != null ? totalTax : 0.0;
        this.grandTotal = grandTotal;
        this.paymentMode = paymentMode;
        this.createdBy = createdBy;
        this.customer = customer;
        this.items = items != null ? items : new ArrayList<>();
    }

    public static InvoiceBuilder builder() {
        return new InvoiceBuilder();
    }

    public static class InvoiceBuilder {
        private Long id;
        private String invoiceNumber;
        private LocalDateTime invoiceDate;
        private Double subTotal;
        private Double totalDiscount = 0.0;
        private Double totalTax = 0.0;
        private Double grandTotal;
        private String paymentMode;
        private User createdBy;
        private Customer customer;
        private List<InvoiceItem> items = new ArrayList<>();

        public InvoiceBuilder id(Long id) { this.id = id; return this; }
        public InvoiceBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public InvoiceBuilder invoiceDate(LocalDateTime invoiceDate) { this.invoiceDate = invoiceDate; return this; }
        public InvoiceBuilder subTotal(Double subTotal) { this.subTotal = subTotal; return this; }
        public InvoiceBuilder totalDiscount(Double totalDiscount) { this.totalDiscount = totalDiscount; return this; }
        public InvoiceBuilder totalTax(Double totalTax) { this.totalTax = totalTax; return this; }
        public InvoiceBuilder grandTotal(Double grandTotal) { this.grandTotal = grandTotal; return this; }
        public InvoiceBuilder paymentMode(String paymentMode) { this.paymentMode = paymentMode; return this; }
        public InvoiceBuilder createdBy(User createdBy) { this.createdBy = createdBy; return this; }
        public InvoiceBuilder customer(Customer customer) { this.customer = customer; return this; }
        public InvoiceBuilder items(List<InvoiceItem> items) { this.items = items; return this; }
        public Invoice build() { return new Invoice(id, invoiceNumber, invoiceDate, subTotal, totalDiscount, totalTax, grandTotal, paymentMode, createdBy, customer, items); }
    }

    public void addItem(InvoiceItem item) {
        if (items == null) items = new ArrayList<>();
        items.add(item);
        item.setInvoice(this);
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

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<InvoiceItem> getItems() { return items; }
    public void setItems(List<InvoiceItem> items) { this.items = items; }
}
