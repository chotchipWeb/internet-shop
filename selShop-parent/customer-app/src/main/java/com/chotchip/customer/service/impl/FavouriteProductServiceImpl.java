package com.chotchip.customer.service.impl;

import com.chotchip.customer.entity.FavouriteProduct;
import com.chotchip.customer.repository.FavouriteProductRepository;
import com.chotchip.customer.service.FavouriteProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavouriteProductServiceImpl implements FavouriteProductService {


    private final FavouriteProductRepository repository;

    @Override
    public Mono<FavouriteProduct> addProductToFavourite(int productId) {
        return repository.save(new FavouriteProduct(UUID.randomUUID(), productId));
    }

    @Override
    public Mono<Void> removeProductToFavourite(int productId) {
        return repository.delete(productId);
    }

    @Override
    public Mono<FavouriteProduct> findFavouriteProductById(int productId) {
        return repository.findById(productId);
    }
}
