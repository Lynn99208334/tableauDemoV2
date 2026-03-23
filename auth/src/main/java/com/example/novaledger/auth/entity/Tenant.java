package com.example.novaledger.auth.entity;

import com.example.novaledger.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "tenants")
public class Tenant extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(nullable = false, length = 20)
    private String plan;

    @Column(name = "owner_user_id", nullable = false)
    private Long ownerUserId;

    @Column(nullable = false, length = 20)
    private String status;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }

    public Long getOwnerUserId() { return ownerUserId; }
    public void setOwnerUserId(Long ownerUserId) { this.ownerUserId = ownerUserId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
