package com.example.tableaudemov2.integration.tenant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TenantHeaderMissingTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    //當 request 沒有帶 X-Tenant-Id header 時，系統必須在 Web 邊界就拒絕請求（401）
    void request_without_tenant_header_should_be_rejected() throws Exception {
        mockMvc.perform(get("/api/debug/tenant"))
                .andExpect(status().isUnauthorized());
    }
}
