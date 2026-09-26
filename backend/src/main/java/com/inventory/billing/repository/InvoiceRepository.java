package com.inventory.billing.repository;

import com.inventory.billing.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    List<Invoice> findByInvoiceDateBetween(LocalDateTime start, LocalDateTime end);

    // Custom JPQL for Total Revenue between dates
    @Query("SELECT SUM(i.grandTotal) FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate")
    Double calculateTotalRevenueBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Custom JPQL for Total Orders Count
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate")
    Long countInvoicesBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
