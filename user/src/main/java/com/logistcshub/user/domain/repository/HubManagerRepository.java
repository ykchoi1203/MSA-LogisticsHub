package com.logistcshub.user.domain.repository;

import com.logistcshub.user.domain.model.HubManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubManagerRepository extends JpaRepository<HubManager, UUID> {
}
