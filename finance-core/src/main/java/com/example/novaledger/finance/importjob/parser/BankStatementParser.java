package com.example.novaledger.finance.importjob.parser;

import java.util.List;

public interface BankStatementParser {

    /**
     * 解析單列資料，回傳解析結果
     * @param rowData 原始列資料（欄位順序依銀行格式）
     * @param rowNumber 列號（用於錯誤訊息）
     * @return ParseResult
     */
    ParseResult parseRow(List<String> rowData, int rowNumber);

    /**
     * 回傳此 parser 對應的 parser_key
     * 必須與 BANK_FILE_FORMATS.parser_key 一致
     */
    String getParserKey();

    /**
     * 回傳此格式的 header 起始列數（0-based）
     * 解析時從這列之後開始讀資料
     */
    int getDataStartRow();
}
