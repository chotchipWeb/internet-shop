package com.chotchip.customer.repository;

import com.chotchip.customer.entity.FavouriteProduct;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FavouriteProductRepository {


    Mono<FavouriteProduct> save(FavouriteProduct favouriteProduct);

    Mono<Void> delete(int productId);

    Mono<FavouriteProduct> findById(int productId);

}
