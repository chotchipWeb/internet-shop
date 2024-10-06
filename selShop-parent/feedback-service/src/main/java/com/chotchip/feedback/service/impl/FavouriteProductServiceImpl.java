package com.chotchip.feedback.service.impl;

import com.chotchip.feedback.entity.FavouriteProduct;
import com.chotchip.feedback.repository.FavouriteProductRepository;
import com.chotchip.feedback.service.FavouriteProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavouriteProductServiceImpl implements FavouriteProductService {


    private final FavouriteProductRepository repository;

    @Override
    public Mono<FavouriteProduct> addProductToFavourite(int productId, String userId) {
        return repository.save(new FavouriteProduct(UUID.randomUUID(), productId, userId));
    }

    @Override
    public Mono<Void> removeProductToFavourite(int productId, String userId) {
        return repository.deleteByProductIdAndUserId(productId, userId);
    }

    @Override
    public Mono<FavouriteProduct> findFavouriteProductById(int productId, String userId) {
        return repository.findByProductIdAndUserId(productId, userId);
    }

    @Override
    public Flux<FavouriteProduct> findAllFavouriteProduct() {
        return repository.findAll();
    }


}
