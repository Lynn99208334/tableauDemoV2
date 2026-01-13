package com.example.tableaudemov2.service;

import org.springframework.stereotype.Service;

@Service
public class TenantDebugService {

    public String whoAmI(Long tenantId) {
        return "current tenantId = " + tenantId;
    }
}

