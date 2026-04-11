package com.example.novaledger.finance.importjob.service;

import com.example.novaledger.common.exception.BusinessException;
import com.example.novaledger.common.exception.ErrorCode;
import com.example.novaledger.finance.importjob.dto.UploadJobResponse;
import com.example.novaledger.finance.importjob.entity.UploadFile;
import com.example.novaledger.finance.importjob.entity.UploadJob;
import com.example.novaledger.finance.importjob.repository.UploadFileRepository;
import com.example.novaledger.finance.importjob.repository.UploadJobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ImportService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final List<String> ALLOWED_EXTENSIONS = List.of("xlsx", "xls", "csv");

    private final UploadJobRepository uploadJobRepository;
    private final UploadFileRepository uploadFileRepository;

    public ImportService(UploadJobRepository uploadJobRepository,
                         UploadFileRepository uploadFileRepository) {
        this.uploadJobRepository = uploadJobRepository;
        this.uploadFileRepository = uploadFileRepository;
    }

    @Transactional
    public UploadJobResponse createUploadJob(MultipartFile file, String jobType, Long tenantId, Long userId) {
        validateFile(file);

        UploadJob job = new UploadJob();
        job.setTenantId(tenantId);
        job.setStatus("PENDING");
        job.setCreatedBy(userId);
        job.setJobType(jobType);
        uploadJobRepository.save(job);

        UploadFile uploadFile = new UploadFile();
        uploadFile.setUploadJobId(job.getId());
        uploadFile.setTenantId(tenantId);
        uploadFile.setOriginalFilename(file.getOriginalFilename());
        uploadFile.setStoredFilename(job.getId() + "_" + file.getOriginalFilename());
        uploadFile.setFileSize(file.getSize());
        uploadFile.setMimeType(file.getContentType());
        uploadFileRepository.save(uploadFile);

        UploadJobResponse response = new UploadJobResponse();
        response.setJobId(job.getId());
        response.setStatus(job.getStatus());
        response.setJobType(job.getJobType());
        response.setOriginalFilename(file.getOriginalFilename());
        response.setCreatedAt(job.getCreatedAt());
        return response;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.FILE_EMPTY);
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(ErrorCode.FILE_TOO_LARGE);
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessException(ErrorCode.FILE_INVALID);
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BusinessException(ErrorCode.FILE_TYPE_NOT_SUPPORTED);
        }
    }
}