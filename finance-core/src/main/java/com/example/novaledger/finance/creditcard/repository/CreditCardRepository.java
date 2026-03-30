package com.example.novaledger.finance.creditcard.repository;

import com.example.novaledger.finance.creditcard.entity.UserCreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<UserCreditCard, Long> {

    List<UserCreditCard> findByTenantIdAndDeletedAtIsNull(Long tenantId);

    Optional<UserCreditCard> findByIdAndTenantIdAndDeletedAtIsNull(Long id, Long tenantId);
}