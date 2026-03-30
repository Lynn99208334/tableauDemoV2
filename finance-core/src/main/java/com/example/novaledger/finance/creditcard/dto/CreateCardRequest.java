package com.example.novaledger.finance.creditcard.dto;

import java.math.BigDecimal;

public class CreateCardRequest {

    private String bankCode;
    private String cardNumberLast4;
    private String cardType;
    private String name;
    private String currencyCode;
    private Integer billingDate;
    private Integer paymentDate;
    private BigDecimal creditLimit;
    private BigDecimal currentBalance;
    private String notes;

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
}