package com.example.novaledger.finance.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardSummaryResponse {

    private Map<String, BigDecimal> totalAssetsByCurrency;
    private BigDecimal totalAssetsInTwd;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpense;
    private List<CategoryBreakdownItem> categoryBreakdown;
    private boolean hasAccounts;

    public Map<String, BigDecimal> getTotalAssetsByCurrency() { return totalAssetsByCurrency; }
    public void setTotalAssetsByCurrency(Map<String, BigDecimal> totalAssetsByCurrency) { this.totalAssetsByCurrency = totalAssetsByCurrency; }

    public BigDecimal getTotalAssetsInTwd() { return totalAssetsInTwd; }
    public void setTotalAssetsInTwd(BigDecimal totalAssetsInTwd) { this.totalAssetsInTwd = totalAssetsInTwd; }

    public BigDecimal getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public BigDecimal getMonthlyExpense() { return monthlyExpense; }
    public void setMonthlyExpense(BigDecimal monthlyExpense) { this.monthlyExpense = monthlyExpense; }

    public List<CategoryBreakdownItem> getCategoryBreakdown() { return categoryBreakdown; }
    public void setCategoryBreakdown(List<CategoryBreakdownItem> categoryBreakdown) { this.categoryBreakdown = categoryBreakdown; }

    public boolean isHasAccounts() { return hasAccounts; }
    public void setHasAccounts(boolean hasAccounts) { this.hasAccounts = hasAccounts; }
}
