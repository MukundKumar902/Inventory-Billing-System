package com.inventory.billing.service;

import com.inventory.billing.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO dto);
    ProductDTO updateProduct(Long id, ProductDTO dto);
    void deleteProduct(Long id);
    ProductDTO getProductById(Long id);
    ProductDTO getProductByBarcode(String barcode);
    List<ProductDTO> getAllProducts();
    List<ProductDTO> searchProducts(String keyword);
    List<ProductDTO> getLowStockProducts();
}
