package com.example.tableaudemov2.controller;

import com.example.tableaudemov2.common.tenant.TenantContext;
import com.example.tableaudemov2.service.TenantDebugService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class TenantDebugController {

    private final TenantDebugService tenantDebugService;

    public TenantDebugController(TenantDebugService tenantDebugService) {
        this.tenantDebugService = tenantDebugService;
    }

    @GetMapping("/tenant")
    public String whoAmI() {
        Long tenantId = TenantContext.getTenantId();
        return tenantDebugService.whoAmI(tenantId);
    }
}

