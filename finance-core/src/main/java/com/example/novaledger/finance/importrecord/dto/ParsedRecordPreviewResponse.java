package com.example.novaledger.finance.importrecord.dto;

public class ParsedRecordPreviewResponse {
    private Long recordId;
    private Integer sourceRowNum;
    private String rawData;
    private String importStatus;

    public ParsedRecordPreviewResponse(Long recordId, Integer sourceRowNum,
                                       String rawData, String importStatus) {
        this.recordId = recordId;
        this.sourceRowNum = sourceRowNum;
        this.rawData = rawData;
        this.importStatus = importStatus;
    }

    public Long getRecordId() { return recordId; }
    public Integer getSourceRowNum() { return sourceRowNum; }
    public String getRawData() { return rawData; }
    public String getImportStatus() { return importStatus; }
}