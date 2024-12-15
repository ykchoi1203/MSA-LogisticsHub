package com.logiticshub.product.application.client;

import com.logiticshub.product.application.dto.CompanyResponseDto;
import com.logiticshub.product.presentation.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "company-service")
public interface CompanyClient {

    @GetMapping("/api/companies/{id}")
    ResponseEntity<ApiResponse<CompanyResponseDto>> getCompany(
            @RequestParam(value = "id", required = false) UUID companyId);

}