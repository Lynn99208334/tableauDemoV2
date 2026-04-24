package com.example.novaledger.finance.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardSummaryResponse {

    private Map<String, BigDecimal> totalAssetsByCurrency;  // 各幣別原始金額
    private Map<String, BigDecimal> assetsByTwd;            // 各幣別換算後 TWD 等值（供幣別佔比圓餅圖）
    private BigDecimal totalAssetsInTwd;                    // 總資產（TWD 合計）
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpense;
    private List<CategoryBreakdownItem> categoryBreakdown;
    private boolean hasAccounts;

    public Map<String, BigDecimal> getTotalAssetsByCurrency() { return totalAssetsByCurrency; }
    public void setTotalAssetsByCurrency(Map<String, BigDecimal> totalAssetsByCurrency) { this.totalAssetsByCurrency = totalAssetsByCurrency; }

    public Map<String, BigDecimal> getAssetsByTwd() { return assetsByTwd; }
    public void setAssetsByTwd(Map<String, BigDecimal> assetsByTwd) { this.assetsByTwd = assetsByTwd; }

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
