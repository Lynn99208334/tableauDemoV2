package com.example.novaledger.finance.importjob.controller;

import com.example.novaledger.common.response.ApiResponse;
import com.example.novaledger.common.tenant.AuthContext;
import com.example.novaledger.finance.importjob.dto.UploadJobResponse;
import com.example.novaledger.finance.importjob.service.ImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Import", description = "Excel 匯入")
@RestController
@RequestMapping("/api/import")
public class ImportController {

    private final ImportService importService;
    private final AuthContext authContext;

    public ImportController(ImportService importService, AuthContext authContext) {
        this.importService = importService;
        this.authContext = authContext;
    }

    @Operation(summary = "上傳 Excel 檔案", description = "支援存摺（ACCOUNT）或信用卡（CREDIT_CARD）格式")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UploadJobResponse>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobType") String jobType,
            HttpServletRequest request) {

        Long tenantId = authContext.getCurrentTenantId(request);
        Long userId = authContext.getCurrentUserId(request);
        UploadJobResponse response = importService.createUploadJob(file, jobType, tenantId, userId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}