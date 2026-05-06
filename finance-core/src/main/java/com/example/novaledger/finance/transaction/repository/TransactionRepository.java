package com.example.novaledger.finance.transaction.repository;

import com.example.novaledger.finance.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByTenantIdAndDeletedAtIsNull(Long tenantId, Pageable pageable);

    Page<Transaction> findByTenantIdAndAccountIdAndDeletedAtIsNull(Long tenantId, Long accountId, Pageable pageable);

    Page<Transaction> findByTenantIdAndAccountIdAndTransactionDateBetweenAndDeletedAtIsNull(
            Long tenantId, Long accountId, LocalDate from, LocalDate to, Pageable pageable);

    Optional<Transaction> findByIdAndTenantIdAndDeletedAtIsNull(Long id, Long tenantId);

    @Query("SELECT COALESCE(SUM(t.totalAmount), 0) FROM Transaction t " +
           "WHERE t.tenantId = :tenantId AND t.txTypeCode = :txTypeCode " +
           "AND t.transactionDate >= :from AND t.transactionDate <= :to " +
           "AND t.deletedAt IS NULL")
    BigDecimal sumByTenantIdAndTxTypeCodeAndDateRange(
            @Param("tenantId") Long tenantId,
            @Param("txTypeCode") String txTypeCode,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);

    @Query("SELECT t FROM Transaction t " +
           "WHERE t.tenantId = :tenantId " +
           "AND t.transactionDate >= :from AND t.transactionDate <= :to " +
           "AND t.deletedAt IS NULL")
    List<Transaction> findByTenantIdAndDateRange(
            @Param("tenantId") Long tenantId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);
}