package com.chotchip.feedback.controller;

import com.chotchip.feedback.entity.FavouriteProduct;
import com.chotchip.feedback.service.FavouriteProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/feedback-api/favourites-products/by-product-id/{productId:\\d+}")
@RequiredArgsConstructor
public class FavouriteProductRestController {

    private final FavouriteProductService favouriteService;

    @PostMapping
    public Mono<ResponseEntity<FavouriteProduct>> addProductToFavourite(@PathVariable("productId") int productId,
                                                                        UriComponentsBuilder uriComponentsBuilder,
                                                                        Mono<JwtAuthenticationToken> authenticationTokenMono) {
        return authenticationTokenMono.flatMap(token -> this.favouriteService.addProductToFavourite(productId, token.getToken().getSubject())
                .map(favouriteProduct -> ResponseEntity
                        .created(uriComponentsBuilder.replacePath("/feedback-api/favourite-products/{id}")
                                .build(favouriteProduct.getProductId()))
                        .body(favouriteProduct)));
    }

    @DeleteMapping
    public Mono<ResponseEntity<Void>> removeProductFromFavourite(@PathVariable("productId") int productId,
                                                                 Mono<JwtAuthenticationToken> authenticationTokenMono) {
        return authenticationTokenMono.flatMap(token -> this.favouriteService.removeProductToFavourite(productId, token.getToken().getSubject())
                .then(Mono.just(ResponseEntity
                        .noContent()
                        .build())));
    }

    @GetMapping
    public Mono<FavouriteProduct> findFavouriteProductByProductId(@PathVariable("productId") int productId,
                                                                  Mono<JwtAuthenticationToken> authenticationTokenMono) {
        return authenticationTokenMono.flatMap(token -> this.favouriteService.findFavouriteProductById(productId, token.getToken().getSubject()));
    }

}
