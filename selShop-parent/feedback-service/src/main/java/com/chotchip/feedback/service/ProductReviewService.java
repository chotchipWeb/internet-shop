package com.chotchip.feedback.service;

import com.chotchip.feedback.dto.ProductReviewCreateDTO;
import com.chotchip.feedback.dto.ProductReviewDisplayDTO;
import com.chotchip.feedback.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewService {

    Mono<ProductReview> createProductReview(int productId, ProductReviewCreateDTO dto,String userId);

    Flux<ProductReviewDisplayDTO> findProductReviewById(int productId);
}
