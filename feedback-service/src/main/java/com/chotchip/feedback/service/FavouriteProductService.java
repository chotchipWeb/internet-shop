package com.chotchip.feedback.service;

import com.chotchip.feedback.entity.FavouriteProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FavouriteProductService {


    Mono<FavouriteProduct> addProductToFavourite(int productId,String userId);

    Mono<Void> removeProductToFavourite(int productId,String userId);


    Mono<FavouriteProduct> findFavouriteProductById(int productId,String userId);

    Flux<FavouriteProduct> findAllFavouriteProduct();
}
