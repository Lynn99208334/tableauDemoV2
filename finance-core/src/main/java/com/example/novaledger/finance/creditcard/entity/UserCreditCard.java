package com.example.novaledger.finance.creditcard.entity;

import com.example.novaledger.common.entity.BaseTenantEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_credit_cards")
public class UserCreditCard extends BaseTenantEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "bank_code", nullable = false, length = 10)
    private String bankCode;

    @Column(name = "card_number_last4", length = 4)
    private String cardNumberLast4;

    @Column(name = "card_type", length = 20)
    private String cardType;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Column(name = "billing_date")
    private Integer billingDate;

    @Column(name = "payment_date")
    private Integer paymentDate;

    @Column(name = "credit_limit", precision = 18, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "current_balance", nullable = false, precision = 18, scale = 2)
    private BigDecimal currentBalance;

    @Column(length = 500)
    private String notes;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }

    public String getCardNumberLast4() { return cardNumberLast4; }
    public void setCardNumberLast4(String cardNumberLast4) { this.cardNumberLast4 = cardNumberLast4; }

    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public Integer getBillingDate() { return billingDate; }
    public void setBillingDate(Integer billingDate) { this.billingDate = billingDate; }

    public Integer getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Integer paymentDate) { this.paymentDate = paymentDate; }

    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }

    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
}