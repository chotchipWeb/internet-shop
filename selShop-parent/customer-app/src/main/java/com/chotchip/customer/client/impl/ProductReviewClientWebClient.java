package com.chotchip.customer.client.impl;

import com.chotchip.customer.client.ProductReviewClient;
import com.chotchip.customer.dto.ProductReviewCreateDTO;
import com.chotchip.customer.dto.ProductReviewDisplayDTO;
import com.chotchip.customer.entity.ProductReview;
import com.chotchip.customer.excpetion.ClientBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class ProductReviewClientWebClient implements ProductReviewClient {

    private final WebClient webClient;

    @Override
    public Flux<ProductReviewDisplayDTO> findAllProductReviewByProductId(int id) {
        return webClient.get()
                .uri("/feedback-api/products-review/by-product-id/{productId}", id)
                .retrieve()
                .bodyToFlux(ProductReviewDisplayDTO.class);
    }

    @Override
    public Mono<ProductReview> createProductReview(int productId, ProductReviewCreateDTO dto) {
        return webClient.post()
                .uri("/feedback-api/products-review/by-product-id/{productId}", productId)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(ProductReview.class)
                .onErrorMap(WebClientResponseException.BadRequest.class, ex ->
                        new ClientBadRequestException(ex,(List<String>) (ex.getResponseBodyAs(ProblemDetail.class).getProperties()
                                .get("errors"))));
    }
}
