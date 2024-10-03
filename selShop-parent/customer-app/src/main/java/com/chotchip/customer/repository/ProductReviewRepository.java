package com.chotchip.customer.repository;

import com.chotchip.customer.dto.ProductReviewDisplayDTO;
import com.chotchip.customer.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewRepository {
    Mono<ProductReview> save(ProductReview productReview);

    Flux<ProductReview> findByProductId(int productId);
}
