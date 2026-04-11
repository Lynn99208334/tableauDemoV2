package com.example.novaledger.finance.importrecord.entity;

import com.example.novaledger.common.entity.BaseTenantEntity;
import com.example.novaledger.finance.enums.ImportStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "parsed_records")
public class ParsedRecord extends BaseTenantEntity {

    @Column(name = "upload_job_id", nullable = false)
    private Long uploadJobId;

    @Column(name = "upload_file_id", nullable = false)
    private Long uploadFileId;

    @Column(name = "source_row_num", nullable = false)
    private Integer sourceRowNum;

    @Column(name = "raw_data", nullable = false, columnDefinition = "JSON")
    private String rawData;

    @Column(name = "parse_status", nullable = false, length = 30)
    private String parseStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "import_status", nullable = false, length = 20)
    private ImportStatus importStatus = ImportStatus.PENDING;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    public ParsedRecord() {}

    public Long getUploadJobId() { return uploadJobId; }
    public void setUploadJobId(Long uploadJobId) { this.uploadJobId = uploadJobId; }

    public Long getUploadFileId() { return uploadFileId; }
    public void setUploadFileId(Long uploadFileId) { this.uploadFileId = uploadFileId; }

    public Integer getSourceRowNum() { return sourceRowNum; }
    public void setSourceRowNum(Integer sourceRowNum) { this.sourceRowNum = sourceRowNum; }

    public String getRawData() { return rawData; }
    public void setRawData(String rawData) { this.rawData = rawData; }

    public String getParseStatus() { return parseStatus; }
    public void setParseStatus(String parseStatus) { this.parseStatus = parseStatus; }

    public ImportStatus getImportStatus() { return importStatus; }
    public void setImportStatus(ImportStatus importStatus) { this.importStatus = importStatus; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}