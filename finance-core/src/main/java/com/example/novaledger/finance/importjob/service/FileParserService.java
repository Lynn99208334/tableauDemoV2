package com.example.novaledger.finance.importjob.service;

import com.example.novaledger.common.exception.BusinessException;
import com.example.novaledger.common.exception.ErrorCode;
import com.example.novaledger.finance.importjob.parser.BankStatementParser;
import com.example.novaledger.finance.importjob.parser.ParseResult;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FileParserService {

    private final Map<String, BankStatementParser> parsers;

    public FileParserService(List<BankStatementParser> parserList) {
        this.parsers = parserList.stream()
                .collect(Collectors.toMap(BankStatementParser::getParserKey, p -> p));
    }

    public List<ParseResult> parse(byte[] fileBytes, String fileName, String parserKey) {
        BankStatementParser parser = parsers.get(parserKey);
        if (parser == null) {
            throw new BusinessException(ErrorCode.IMPORT_PARSER_NOT_FOUND);
        }

        List<List<String>> rows = readRows(fileBytes, fileName);
        List<ParseResult> results = new ArrayList<>();

        int dataStartRow = parser.getDataStartRow();
        for (int i = dataStartRow; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            // 跳過全空列
            if (row.stream().allMatch(cell -> cell.trim().isEmpty())) {
                continue;
            }
            results.add(parser.parseRow(row, i + 1));
        }

        return results;
    }

    private List<List<String>> readRows(byte[] fileBytes, String fileName) {
        String lowerName = fileName.toLowerCase();
        if (lowerName.endsWith(".csv")) {
            return readCsv(fileBytes);
        } else {
            // XLS 或 XLSX：先判斷是否為 HTML-as-XLS
            String header = new String(Arrays.copyOf(fileBytes, Math.min(fileBytes.length, 16)));
            if (header.contains("<!") || header.toLowerCase().contains("<html")) {
                return readHtmlAsXls(fileBytes);
            }
            throw new BusinessException(ErrorCode.FILE_TYPE_NOT_SUPPORTED);
        }
    }

    private List<List<String>> readCsv(byte[] fileBytes) {
        try {
            // 偵測編碼（Big5 或 UTF-8）
            String content;
            String headerStr = new String(Arrays.copyOf(fileBytes, Math.min(fileBytes.length, 3)));
            if (headerStr.startsWith("\uFEFF")) {
                content = new String(fileBytes, StandardCharsets.UTF_8).substring(1);
            } else {
                try {
                    content = new String(fileBytes, "Big5");
                } catch (Exception e) {
                    content = new String(fileBytes, StandardCharsets.UTF_8);
                }
            }

            List<List<String>> rows = new ArrayList<>();
            String[] lines = content.split("\r\n|\n");
            for (String line : lines) {
                List<String> cells = parseCsvLine(line);
                rows.add(cells);
            }
            return rows;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.IMPORT_FILE_READ_FAILED);
        }
    }

    private List<String> parseCsvLine(String line) {
        List<String> cells = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                cells.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        cells.add(sb.toString().trim());
        return cells;
    }

    private List<List<String>> readHtmlAsXls(byte[] fileBytes) {
        try {
            String content = new String(fileBytes, StandardCharsets.UTF_8);
            List<List<String>> rows = new ArrayList<>();

            // 找資料 table（有多個 tr 的那個）
            int tableStart = -1;
            int searchFrom = 0;
            while (true) {
                int idx = content.toLowerCase().indexOf("<table", searchFrom);
                if (idx == -1) break;
                int tableEnd = content.toLowerCase().indexOf("</table>", idx);
                String tableContent = content.substring(idx, tableEnd);
                long trCount = tableContent.toLowerCase().chars()
                        .filter(c -> tableContent.toLowerCase().indexOf("<tr", 0) >= 0)
                        .count();
                // 用 tr 數量判斷是否為資料 table
                if (tableContent.split("(?i)<tr").length > 3) {
                    tableStart = idx;
                    break;
                }
                searchFrom = tableEnd + 1;
            }

            if (tableStart == -1) {
                throw new BusinessException(ErrorCode.IMPORT_HTML_PARSE_FAILED);
            }

            int tableEnd = content.toLowerCase().indexOf("</table>", tableStart);
            String tableContent = content.substring(tableStart, tableEnd);
            String[] trParts = tableContent.split("(?i)<tr[^>]*>");

            for (String tr : trParts) {
                if (tr.trim().isEmpty()) continue;
                List<String> cells = new ArrayList<>();
                String[] tdParts = tr.split("(?i)<td[^>]*>");
                for (String td : tdParts) {
                    if (td.trim().isEmpty()) continue;
                    // 移除 HTML tags
                    String cell = td.replaceAll("<[^>]+>", "").trim();
                    // decode HTML entities
                    cell = cell.replace("&nbsp;", "").replace("&amp;", "&")
                            .replace("&lt;", "<").replace("&gt;", ">");
                    cells.add(cell);
                }
                if (!cells.isEmpty()) {
                    rows.add(cells);
                }
            }
            return rows;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.IMPORT_FILE_READ_FAILED);
        }
    }
}
