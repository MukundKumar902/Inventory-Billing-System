package com.inventory.billing.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductDTO {
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    private String barcode;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private Double price;

    private Double gstPercent = 0.0;

    @NotNull(message = "Current stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer currentStock;

    private Integer minStockAlert = 5;

    private String unit;

    private String imageUrl;

    private Long categoryId;
    private String categoryName;

    private Long supplierId;
    private String supplierName;

    public ProductDTO() {}

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

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
}
