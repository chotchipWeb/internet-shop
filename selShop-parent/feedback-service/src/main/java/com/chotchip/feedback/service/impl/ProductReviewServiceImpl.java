package com.chotchip.feedback.service.impl;

import com.chotchip.feedback.dto.ProductReviewCreateDTO;
import com.chotchip.feedback.dto.ProductReviewDisplayDTO;
import com.chotchip.feedback.entity.ProductReview;
import com.chotchip.feedback.repository.ProductReviewRepository;
import com.chotchip.feedback.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductReviewRepository repository;

    @Override
    public Mono<ProductReview> createProductReview(int productId, ProductReviewCreateDTO dto, String userId) {
        return repository.save(new ProductReview(UUID.randomUUID(), productId, dto.getRating(), dto.getDetails(), userId));
    }

    @Override
    public Flux<ProductReviewDisplayDTO> findProductReviewById(int productId) {
        Flux<ProductReview> byProductId = repository.findByProductId(productId);
        return byProductId.map(productReview -> new ProductReviewDisplayDTO(productReview.getRating(), productReview.getDetails()))
                .ofType(ProductReviewDisplayDTO.class);
    }
}
