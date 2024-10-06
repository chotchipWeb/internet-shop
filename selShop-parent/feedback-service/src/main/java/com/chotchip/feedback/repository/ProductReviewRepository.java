package com.chotchip.feedback.repository;

import com.chotchip.feedback.entity.ProductReview;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface ProductReviewRepository extends ReactiveCrudRepository<ProductReview, UUID> {
    Flux<ProductReview> findByProductId(int productId);
}
