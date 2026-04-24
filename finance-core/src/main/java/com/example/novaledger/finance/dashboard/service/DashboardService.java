package com.example.novaledger.finance.dashboard.service;

import com.example.novaledger.finance.account.entity.UserAccount;
import com.example.novaledger.finance.account.repository.UserAccountRepository;
import com.example.novaledger.finance.dashboard.dto.CategoryBreakdownItem;
import com.example.novaledger.finance.dashboard.dto.DashboardSummaryResponse;
import com.example.novaledger.finance.exchangerate.repository.ExchangeRateRepository;
import com.example.novaledger.finance.transaction.entity.Transaction;
import com.example.novaledger.finance.transaction.entity.TransactionItem;
import com.example.novaledger.finance.transaction.repository.TransactionItemRepository;
import com.example.novaledger.finance.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final UserAccountRepository userAccountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionItemRepository transactionItemRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    public DashboardService(
            UserAccountRepository userAccountRepository,
            TransactionRepository transactionRepository,
            TransactionItemRepository transactionItemRepository,
            ExchangeRateRepository exchangeRateRepository) {
        this.userAccountRepository = userAccountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionItemRepository = transactionItemRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public DashboardSummaryResponse getDashboardSummary(Long tenantId) {
        DashboardSummaryResponse response = new DashboardSummaryResponse();

        // 1. 總資產（依幣別分組）
        List<UserAccount> accounts = userAccountRepository.findByTenantIdAndDeletedAtIsNull(tenantId);
        response.setHasAccounts(!accounts.isEmpty());

        Map<String, BigDecimal> totalAssetsByCurrency = accounts.stream()
                .collect(Collectors.groupingBy(
                        UserAccount::getCurrencyCode,
                        Collectors.reducing(BigDecimal.ZERO, UserAccount::getCurrentBalance, BigDecimal::add)
                ));
        response.setTotalAssetsByCurrency(totalAssetsByCurrency);

        // 2. 總資產換算成 TWD
        BigDecimal totalInTwd = calculateTotalInTwd(totalAssetsByCurrency);
        response.setTotalAssetsInTwd(totalInTwd);

        // 3. 本月收支
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate today = LocalDate.now();

        BigDecimal income = transactionRepository.sumByTenantIdAndTxTypeCodeAndDateRange(
                tenantId, "INCOME", firstDayOfMonth, today);
        BigDecimal expense = transactionRepository.sumByTenantIdAndTxTypeCodeAndDateRange(
                tenantId, "EXPENSE", firstDayOfMonth, today);

        response.setMonthlyIncome(income != null ? income : BigDecimal.ZERO);
        response.setMonthlyExpense(expense != null ? expense : BigDecimal.ZERO);

        // 4. 分類佔比（本月支出）
        List<CategoryBreakdownItem> breakdown = calculateCategoryBreakdown(tenantId, firstDayOfMonth, today);
        response.setCategoryBreakdown(breakdown);

        return response;
    }

    private BigDecimal calculateTotalInTwd(Map<String, BigDecimal> totalAssetsByCurrency) {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<String, BigDecimal> entry : totalAssetsByCurrency.entrySet()) {
            String currency = entry.getKey();
            BigDecimal amount = entry.getValue();
            if ("TWD".equals(currency)) {
                total = total.add(amount);
            } else {
                BigDecimal rate = exchangeRateRepository
                        .findTopByBaseCurrencyAndQuoteCurrencyOrderByRateDateDesc(currency, "TWD")
                        .map(er -> er.getRate())
                        .orElse(BigDecimal.ONE);
                total = total.add(amount.multiply(rate).setScale(2, RoundingMode.HALF_UP));
            }
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private List<CategoryBreakdownItem> calculateCategoryBreakdown(Long tenantId, LocalDate from, LocalDate to) {
        List<Transaction> expenseTransactions = transactionRepository
                .findByTenantIdAndDateRange(tenantId, from, to)
                .stream()
                .filter(t -> "EXPENSE".equals(t.getTxTypeCode()))
                .collect(Collectors.toList());

        Map<Long, BigDecimal> categoryAmountMap = new HashMap<>();
        for (Transaction tx : expenseTransactions) {
            List<TransactionItem> items = transactionItemRepository
                    .findByTenantIdAndTransactionId(tenantId, tx.getId());
            for (TransactionItem item : items) {
                Long catId = item.getCategoryId() != null ? item.getCategoryId() : 0L;
                categoryAmountMap.merge(catId, item.getAmount(), BigDecimal::add);
            }
        }

        return categoryAmountMap.entrySet().stream()
                .map(e -> new CategoryBreakdownItem(
                        e.getKey(),
                        e.getKey() == 0L ? "未分類" : "分類 #" + e.getKey(),
                        e.getValue()))
                .sorted(Comparator.comparing(CategoryBreakdownItem::getAmount).reversed())
                .collect(Collectors.toList());
    }
}
