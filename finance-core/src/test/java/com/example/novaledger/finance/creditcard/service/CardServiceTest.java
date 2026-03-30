package com.example.novaledger.finance.creditcard.service;

import com.example.novaledger.common.exception.BusinessException;
import com.example.novaledger.common.exception.ErrorCode;
import com.example.novaledger.common.tenant.TenantContext;
import com.example.novaledger.finance.creditcard.dto.CardResponse;
import com.example.novaledger.finance.creditcard.dto.CreateCardRequest;
import com.example.novaledger.finance.creditcard.dto.UpdateCardRequest;
import com.example.novaledger.finance.creditcard.entity.UserCreditCard;
import com.example.novaledger.finance.creditcard.repository.CreditCardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private CardService cardService;

    private static final Long TENANT_ID = 1L;
    private static final Long USER_ID = 10L;
    private static final Long CARD_ID = 100L;

    @BeforeEach
    void setUp() {
        TenantContext.setTenantId(TENANT_ID);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void should_create_card_and_return_response_when_request_is_valid() {
        // arrange
        CreateCardRequest request = new CreateCardRequest();
        request.setBankCode("007");
        request.setName("我的信用卡");
        request.setCurrencyCode("TWD");
        request.setCurrentBalance(BigDecimal.ZERO);

        UserCreditCard saved = new UserCreditCard();
        saved.setTenantId(TENANT_ID);
        saved.setUserId(USER_ID);
        saved.setBankCode("007");
        saved.setName("我的信用卡");
        saved.setCurrencyCode("TWD");
        saved.setCurrentBalance(BigDecimal.ZERO);

        when(creditCardRepository.save(any())).thenReturn(saved);

        // act
        CardResponse response = cardService.createCard(USER_ID, request);

        // assert
        assertThat(response.getBankCode()).isEqualTo("007");
        assertThat(response.getName()).isEqualTo("我的信用卡");
        assertThat(response.getCurrencyCode()).isEqualTo("TWD");
    }

    @Test
    void should_set_deleted_at_when_delete_card() {
        // arrange
        UserCreditCard card = new UserCreditCard();
        card.setTenantId(TENANT_ID);
        card.setUserId(USER_ID);

        when(creditCardRepository.findByIdAndTenantIdAndDeletedAtIsNull(CARD_ID, TENANT_ID))
                .thenReturn(Optional.of(card));
        when(creditCardRepository.save(any())).thenReturn(card);

        // act
        cardService.deleteCard(USER_ID, CARD_ID);

        // assert
        ArgumentCaptor<UserCreditCard> captor = ArgumentCaptor.forClass(UserCreditCard.class);
        verify(creditCardRepository).save(captor.capture());
        assertThat(captor.getValue().getDeletedAt()).isNotNull();
    }

    @Test
    void should_throw_when_card_not_found_on_update() {
        // arrange
        when(creditCardRepository.findByIdAndTenantIdAndDeletedAtIsNull(CARD_ID, TENANT_ID))
                .thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> cardService.updateCard(USER_ID, CARD_ID, new UpdateCardRequest()))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> assertThat(((BusinessException) e).getErrorCode())
                        .isEqualTo(ErrorCode.CARD_001));
    }
}
