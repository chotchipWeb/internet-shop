package com.chotchip.customer.service;

import com.chotchip.customer.dto.ProductReviewCreateDTO;
import com.chotchip.customer.dto.ProductReviewDisplayDTO;
import com.chotchip.customer.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewService {

    Mono<ProductReview> createProductReview(int productId,ProductReviewCreateDTO dto);

    Flux<ProductReviewDisplayDTO> findProductReviewById(int productId);
}
