package com.chotchip.feedback.repository;

import com.chotchip.feedback.entity.FavouriteProduct;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface FavouriteProductRepository extends ReactiveCrudRepository<FavouriteProduct, UUID> {
    Mono<Void> deleteByProductIdAndUserId(int productId,String userId);

    Mono<FavouriteProduct> findByProductIdAndUserId(int productId,String userId);


}
