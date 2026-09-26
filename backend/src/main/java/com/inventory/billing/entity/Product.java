package com.inventory.billing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String barcode;

    @Column(nullable = false)
    private Double price;

    @Column(name = "gst_percent")
    private Double gstPercent = 0.0;

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock = 0;

    @Column(name = "min_stock_alert", nullable = false)
    private Integer minStockAlert = 5;

    private String unit;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    public Product() {}

    public Product(Long id, String name, String barcode, Double price, Double gstPercent, Integer currentStock, Integer minStockAlert, String unit, String imageUrl, Category category, Supplier supplier) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.price = price;
        this.gstPercent = gstPercent != null ? gstPercent : 0.0;
        this.currentStock = currentStock != null ? currentStock : 0;
        this.minStockAlert = minStockAlert != null ? minStockAlert : 5;
        this.unit = unit;
        this.imageUrl = imageUrl;
        this.category = category;
        this.supplier = supplier;
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {
        private Long id;
        private String name;
        private String barcode;
        private Double price;
        private Double gstPercent = 0.0;
        private Integer currentStock = 0;
        private Integer minStockAlert = 5;
        private String unit;
        private String imageUrl;
        private Category category;
        private Supplier supplier;

        public ProductBuilder id(Long id) { this.id = id; return this; }
        public ProductBuilder name(String name) { this.name = name; return this; }
        public ProductBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public ProductBuilder price(Double price) { this.price = price; return this; }
        public ProductBuilder gstPercent(Double gstPercent) { this.gstPercent = gstPercent; return this; }
        public ProductBuilder currentStock(Integer currentStock) { this.currentStock = currentStock; return this; }
        public ProductBuilder minStockAlert(Integer minStockAlert) { this.minStockAlert = minStockAlert; return this; }
        public ProductBuilder unit(String unit) { this.unit = unit; return this; }
        public ProductBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public ProductBuilder category(Category category) { this.category = category; return this; }
        public ProductBuilder supplier(Supplier supplier) { this.supplier = supplier; return this; }
        public Product build() { return new Product(id, name, barcode, price, gstPercent, currentStock, minStockAlert, unit, imageUrl, category, supplier); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getGstPercent() { return gstPercent; }
    public void setGstPercent(Double gstPercent) { this.gstPercent = gstPercent; }

    public Integer getCurrentStock() { return currentStock; }
    public void setCurrentStock(Integer currentStock) { this.currentStock = currentStock; }

    public Integer getMinStockAlert() { return minStockAlert; }
    public void setMinStockAlert(Integer minStockAlert) { this.minStockAlert = minStockAlert; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }
}
