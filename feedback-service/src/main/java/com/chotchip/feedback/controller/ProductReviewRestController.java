package com.chotchip.feedback.controller;

import com.chotchip.feedback.dto.ProductReviewCreateDTO;
import com.chotchip.feedback.dto.ProductReviewDisplayDTO;
import com.chotchip.feedback.entity.ProductReview;
import com.chotchip.feedback.service.ProductReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/feedback-api/products-review/by-product-id/{productId:\\d+}")
@RequiredArgsConstructor
public class ProductReviewRestController {

    private final ProductReviewService reviewService;

    @PostMapping()
    public Mono<ResponseEntity<ProductReview>> createProductReview(@PathVariable("productId") int productId,
                                                                   @Valid @RequestBody Mono<ProductReviewCreateDTO> reviewDTO,
                                                                   UriComponentsBuilder uriComponentsBuilder,
                                                                   Mono<JwtAuthenticationToken> authenticationTokenMono) {
        return authenticationTokenMono.flatMap(token -> reviewDTO.
                        flatMap(reviewCreateDTO -> this.reviewService.createProductReview(productId, reviewCreateDTO, token.getToken().getSubject())))
                .map(productReview -> ResponseEntity
                        .created(uriComponentsBuilder.replacePath("/feedback-api/products-review/{id}")
                                .build(productReview.getId()))
                        .body(productReview)
                );
    }

    @GetMapping()
    @Operation(
            security = @SecurityRequirement(name = "keycloak")
    )
    public Flux<ProductReviewDisplayDTO> findAllProductReviewByProductId(@PathVariable("productId") int productId) {
        return this.reviewService.findProductReviewById(productId);
    }
}
