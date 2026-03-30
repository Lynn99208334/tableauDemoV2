package com.example.novaledger.finance.creditcard.service;

import com.example.novaledger.common.exception.BusinessException;
import com.example.novaledger.common.exception.ErrorCode;
import com.example.novaledger.common.tenant.TenantContext;
import com.example.novaledger.finance.creditcard.dto.CardResponse;
import com.example.novaledger.finance.creditcard.dto.CreateCardRequest;
import com.example.novaledger.finance.creditcard.dto.UpdateCardRequest;
import com.example.novaledger.finance.creditcard.entity.UserCreditCard;
import com.example.novaledger.finance.creditcard.repository.CreditCardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CardService {

    private final CreditCardRepository creditCardRepository;

    public CardService(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    @Transactional
    public CardResponse createCard(Long userId, CreateCardRequest request) {
        Long tenantId = TenantContext.getTenantId();

        UserCreditCard card = new UserCreditCard();
        card.setTenantId(tenantId);
        card.setUserId(userId);
        card.setBankCode(request.getBankCode());
        card.setCardNumberLast4(request.getCardNumberLast4());
        card.setCardType(request.getCardType());
        card.setName(request.getName());
        card.setCurrencyCode(request.getCurrencyCode());
        card.setBillingDate(request.getBillingDate());
        card.setPaymentDate(request.getPaymentDate());
        card.setCreditLimit(request.getCreditLimit());
        card.setCurrentBalance(request.getCurrentBalance());
        card.setNotes(request.getNotes());

        return CardResponse.from(creditCardRepository.save(card));
    }

    public List<CardResponse> getCards(Long userId) {
        Long tenantId = TenantContext.getTenantId();
        return creditCardRepository
                .findByTenantIdAndDeletedAtIsNull(tenantId)
                .stream()
                .filter(c -> c.getUserId().equals(userId))
                .map(CardResponse::from)
                .toList();
    }

    @Transactional
    public CardResponse updateCard(Long userId, Long cardId, UpdateCardRequest request) {
        Long tenantId = TenantContext.getTenantId();
        UserCreditCard card = creditCardRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(cardId, tenantId)
                .filter(c -> c.getUserId().equals(userId))
                .orElseThrow(() -> new BusinessException(ErrorCode.CARD_001));

        card.setBankCode(request.getBankCode());
        card.setCardNumberLast4(request.getCardNumberLast4());
        card.setCardType(request.getCardType());
        card.setName(request.getName());
        card.setCurrencyCode(request.getCurrencyCode());
        card.setBillingDate(request.getBillingDate());
        card.setPaymentDate(request.getPaymentDate());
        card.setCreditLimit(request.getCreditLimit());
        card.setNotes(request.getNotes());

        return CardResponse.from(creditCardRepository.save(card));
    }

    @Transactional
    public void deleteCard(Long userId, Long cardId) {
        Long tenantId = TenantContext.getTenantId();
        UserCreditCard card = creditCardRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(cardId, tenantId)
                .filter(c -> c.getUserId().equals(userId))
                .orElseThrow(() -> new BusinessException(ErrorCode.CARD_001));

        card.setDeletedAt(LocalDateTime.now());
        creditCardRepository.save(card);
    }
}