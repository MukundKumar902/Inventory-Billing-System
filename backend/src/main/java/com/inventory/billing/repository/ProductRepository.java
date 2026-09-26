package com.inventory.billing.repository;

import com.inventory.billing.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByBarcode(String barcode);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Custom JPQL Query for Low Stock Alert (Products where stock <= min_stock_alert)
    @Query("SELECT p FROM Product p WHERE p.currentStock <= p.minStockAlert")
    List<Product> findLowStockProducts();

    // Search by category
    List<Product> findByCategoryId(Long categoryId);
}
