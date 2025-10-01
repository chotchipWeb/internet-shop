package com.chotchip.customer.client;

import com.chotchip.customer.dto.ProductReviewCreateDTO;
import com.chotchip.customer.dto.ProductReviewDisplayDTO;
import com.chotchip.customer.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewClient {


    Flux<ProductReviewDisplayDTO> findAllProductReviewByProductId(int id);

    Mono<ProductReview> createProductReview(int productId, ProductReviewCreateDTO dto);
}
