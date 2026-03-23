package com.example.novaledger.auth.repository;

import com.example.novaledger.auth.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
//    Optional<Tenant> findBySlug(String slug);
}

