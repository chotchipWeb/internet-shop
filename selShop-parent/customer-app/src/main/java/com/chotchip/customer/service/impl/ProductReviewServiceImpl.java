package com.chotchip.customer.service.impl;

import com.chotchip.customer.dto.ProductReviewCreateDTO;
import com.chotchip.customer.dto.ProductReviewDisplayDTO;
import com.chotchip.customer.entity.ProductReview;
import com.chotchip.customer.repository.ProductReviewRepository;
import com.chotchip.customer.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductReviewRepository repository;

    @Override
    public Mono<ProductReview> createProductReview(int productId, ProductReviewCreateDTO dto) {
        return repository.save(new ProductReview(null, productId, dto.getRating(), dto.getDetails()));
    }

    @Override
    public Flux<ProductReviewDisplayDTO> findProductReviewById(int productId) {
        Flux<ProductReview> byProductId = repository.findByProductId(productId);
        return byProductId.map(productReview -> new ProductReviewDisplayDTO(productReview.getRating(), productReview.getDetails()))
                .ofType(ProductReviewDisplayDTO.class);
    }
}
