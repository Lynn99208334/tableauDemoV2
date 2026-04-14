package com.example.novaledger.finance.importjob.controller;

import com.example.novaledger.finance.importjob.parser.CtbcBankStatementParser;
import com.example.novaledger.finance.importjob.parser.ParseResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CtbcBankStatementParserTest {

    private CtbcBankStatementParser parser;

    @BeforeEach
    void setUp() {
        parser = new CtbcBankStatementParser();
    }

    @Test
    @DisplayName("正常支出列 → 解析成功，amount 為負值")
    void parseRow_validWithdrawal_success() {
        List<String> row = List.of("2026/3/3", "中信卡", "678", "", "0");

        ParseResult result = parser.parseRow(row, 5);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getTransactionDate()).isEqualTo(LocalDate.of(2026, 3, 3));
        assertThat(result.getDescription()).isEqualTo("中信卡");
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("-678"));
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("0"));
    }

    @Test
    @DisplayName("正常存入列 → 解析成功，amount 為正值")
    void parseRow_validDeposit_success() {
        List<String> row = List.of("2026/3/3", "跨行轉", "", "49000", "49000");

        ParseResult result = parser.parseRow(row, 6);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("49000"));
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("49000"));
    }

    @Test
    @DisplayName("金額格式錯誤 → 解析失敗，errorMessage 包含關鍵字")
    void parseRow_invalidAmount_failure() {
        List<String> row = List.of("2026/3/3", "中信卡", "abc", "", "0");

        ParseResult result = parser.parseRow(row, 5);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrorMessage()).contains("金額格式錯誤");
    }

    @Test
    @DisplayName("日期格式錯誤 → 解析失敗，errorMessage 包含關鍵字")
    void parseRow_invalidDate_failure() {
        List<String> row = List.of("20260303", "中信卡", "678", "", "0");

        ParseResult result = parser.parseRow(row, 5);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrorMessage()).contains("日期格式錯誤");
    }

    @Test
    @DisplayName("欄位數不足 → 解析失敗")
    void parseRow_insufficientColumns_failure() {
        List<String> row = List.of("2026/3/3", "中信卡");

        ParseResult result = parser.parseRow(row, 5);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrorMessage()).contains("欄位數不足");
    }
}