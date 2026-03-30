package com.example.novaledger.finance.creditcard.dto;

import com.example.novaledger.finance.creditcard.entity.UserCreditCard;

import java.math.BigDecimal;

public class CardResponse {

    private Long id;
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

    public static CardResponse from(UserCreditCard card) {
        CardResponse response = new CardResponse();
        response.id = card.getId();
        response.bankCode = card.getBankCode();
        response.cardNumberLast4 = card.getCardNumberLast4();
        response.cardType = card.getCardType();
        response.name = card.getName();
        response.currencyCode = card.getCurrencyCode();
        response.billingDate = card.getBillingDate();
        response.paymentDate = card.getPaymentDate();
        response.creditLimit = card.getCreditLimit();
        response.currentBalance = card.getCurrentBalance();
        response.notes = card.getNotes();
        return response;
    }

    public Long getId() { return id; }
    public String getBankCode() { return bankCode; }
    public String getCardNumberLast4() { return cardNumberLast4; }
    public String getCardType() { return cardType; }
    public String getName() { return name; }
    public String getCurrencyCode() { return currencyCode; }
    public Integer getBillingDate() { return billingDate; }
    public Integer getPaymentDate() { return paymentDate; }
    public BigDecimal getCreditLimit() { return creditLimit; }
    public BigDecimal getCurrentBalance() { return currentBalance; }
    public String getNotes() { return notes; }
}