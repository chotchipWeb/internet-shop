package com.chotchip.customer.client.impl;

import com.chotchip.customer.client.FavouriteProductClient;
import com.chotchip.customer.entity.FavouriteProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FavouriteProductClientWebClient implements FavouriteProductClient {
    private final WebClient webClient;

    @Override
    public Mono<FavouriteProduct> findFavouriteProductById(int id) {
        return this.webClient.get()
                .uri("/feedback-api/favourites-products/by-product-id/{productId}", id)
                .retrieve()
                .bodyToMono(FavouriteProduct.class);
    }

    @Override
    public Mono<FavouriteProduct> addProductToFavourite(Integer productId) {
        return this.webClient.post()
                .uri("/feedback-api/favourites-products/by-product-id/{productId}", productId)
                .retrieve()
                .bodyToMono(FavouriteProduct.class);
    }

    @Override
    public Mono<FavouriteProduct> removeProductToFavourite(Integer productId) {
        return this.webClient.delete()
                .uri("/feedback-api/favourites-products/by-product-id/{productId}", productId)
                .retrieve()
                .bodyToMono(FavouriteProduct.class);
    }
}
