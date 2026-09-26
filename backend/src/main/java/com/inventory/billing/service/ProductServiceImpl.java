package com.inventory.billing.service;

import com.inventory.billing.dto.ProductDTO;
import com.inventory.billing.entity.Category;
import com.inventory.billing.entity.Product;
import com.inventory.billing.entity.Supplier;
import com.inventory.billing.exception.ResourceNotFoundException;
import com.inventory.billing.repository.CategoryRepository;
import com.inventory.billing.repository.ProductRepository;
import com.inventory.billing.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO dto) {
        Product product = mapToEntity(dto);
        Product saved = productRepository.save(product);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        existing.setName(dto.getName());
        existing.setBarcode(dto.getBarcode());
        existing.setPrice(dto.getPrice());
        existing.setGstPercent(dto.getGstPercent() != null ? dto.getGstPercent() : 0.0);
        existing.setCurrentStock(dto.getCurrentStock());
        existing.setMinStockAlert(dto.getMinStockAlert() != null ? dto.getMinStockAlert() : 5);
        existing.setUnit(dto.getUnit());
        existing.setImageUrl(dto.getImageUrl());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            existing.setCategory(category);
        }

        if (dto.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId()).orElse(null);
            existing.setSupplier(supplier);
        }

        Product updated = productRepository.save(existing);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToDTO(product);
    }

    @Override
    public ProductDTO getProductByBarcode(String barcode) {
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with barcode: " + barcode));
        return mapToDTO(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getLowStockProducts() {
        return productRepository.findLowStockProducts().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private Product mapToEntity(ProductDTO dto) {
        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        }

        Supplier supplier = null;
        if (dto.getSupplierId() != null) {
            supplier = supplierRepository.findById(dto.getSupplierId()).orElse(null);
        }

        return Product.builder()
                .name(dto.getName())
                .barcode(dto.getBarcode())
                .price(dto.getPrice())
                .gstPercent(dto.getGstPercent() != null ? dto.getGstPercent() : 0.0)
                .currentStock(dto.getCurrentStock())
                .minStockAlert(dto.getMinStockAlert() != null ? dto.getMinStockAlert() : 5)
                .unit(dto.getUnit())
                .imageUrl(dto.getImageUrl())
                .category(category)
                .supplier(supplier)
                .build();
    }

    private ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setBarcode(product.getBarcode());
        dto.setPrice(product.getPrice());
        dto.setGstPercent(product.getGstPercent());
        dto.setCurrentStock(product.getCurrentStock());
        dto.setMinStockAlert(product.getMinStockAlert());
        dto.setUnit(product.getUnit());
        dto.setImageUrl(product.getImageUrl());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }

        if (product.getSupplier() != null) {
            dto.setSupplierId(product.getSupplier().getId());
            dto.setSupplierName(product.getSupplier().getName());
        }

        return dto;
    }
}
