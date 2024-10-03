package com.chotchip.customer.service;

import com.chotchip.customer.entity.FavouriteProduct;
import com.chotchip.customer.entity.Product;
import reactor.core.publisher.Mono;

public interface FavouriteProductService {


    Mono<FavouriteProduct> addProductToFavourite(int productId);

    Mono<Void> removeProductToFavourite(int productId);


    Mono<FavouriteProduct> findFavouriteProductById(int productId);
}
