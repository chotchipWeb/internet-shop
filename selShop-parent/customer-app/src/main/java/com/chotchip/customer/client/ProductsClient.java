package com.chotchip.customer.client;

import com.chotchip.customer.entity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductsClient {

    Flux<Product> findAllProducts();

    Mono<Product> findProductById(int id);
}
