package com.logiticshub.product.domain.service;

import com.logiticshub.product.application.dto.ProductResponseDto;
import com.logiticshub.product.domain.model.Product;
import com.logiticshub.product.domain.repository.ProductRepository;
import com.logiticshub.product.presentation.request.ProductRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.logiticshub.product.domain.model.QProduct.product;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDto createProduct(Long userId, String role, ProductRequestDto productRequestDto) {
        Product product = productRequestDto.toEntity();
        product.create(userId);

        productRepository.save(product);
        return ProductResponseDto.toDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(UUID id, Long userId, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("입력한 id값을 가진 상품이 존재하지 않습니다."));
        product.update(userId, productRequestDto);
        productRepository.save(product);
        return ProductResponseDto.toDto(product);
    }

    @Transactional
    public ProductResponseDto deleteProduct(UUID id, Long userId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("입력한 id값을 가진 상품이 존재하지 않습니다."));
        product.delete(userId);
        return ProductResponseDto.toDto(product);
    }

    @Transactional(readOnly = true)
    public PagedModel<ProductResponseDto> getProducts(Predicate predicate, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder(predicate);

        booleanBuilder.and(product.isDelete.isFalse());

        Page<Product> products = productRepository.findAll(booleanBuilder, pageable);
        return new PagedModel<>(products.map(ProductResponseDto::toDto));
    }
}