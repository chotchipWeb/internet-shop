package com.chotchip.customer.client.impl;

import com.chotchip.customer.client.ProductsClient;
import com.chotchip.customer.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductsClientImpl implements ProductsClient {

    private final WebClient webClient;

    @Override
    public Flux<Product> findAllProducts() {
        return webClient.get()
                .uri("catalogue-api/products")
                .retrieve()
                .bodyToFlux(Product.class);
    }

    @Override
    public Mono<Product> findProductById(int id) {
        return webClient.get()
                .uri("/catalogue-api/products/{productId}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }
}
