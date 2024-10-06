package com.chotchip.customer.client;

import com.chotchip.customer.entity.FavouriteProduct;
import reactor.core.publisher.Mono;

public interface FavouriteProductClient {

    Mono<FavouriteProduct> findFavouriteProductById(int id);

    Mono<FavouriteProduct> addProductToFavourite(Integer productId);

    Mono<FavouriteProduct> removeProductToFavourite(Integer productId);
}
