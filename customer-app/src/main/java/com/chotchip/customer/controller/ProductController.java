package com.chotchip.customer.controller;

import com.chotchip.customer.client.FavouriteProductClient;
import com.chotchip.customer.client.ProductReviewClient;
import com.chotchip.customer.client.ProductsClient;
import com.chotchip.customer.dto.ProductReviewCreateDTO;
import com.chotchip.customer.entity.Product;
import com.chotchip.customer.excpetion.ClientBadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.reactive.result.view.CsrfRequestDataValueProcessor;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer/products/{productId:\\d+}")
@Slf4j
public class ProductController {

    private final ProductsClient productsClient;
    private final FavouriteProductClient favouriteProductService;
    private final ProductReviewClient productReviewService;


    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable("productId") int id) {
        return this.productsClient.findProductById(id)
                .switchIfEmpty(Mono.error(new NoSuchElementException("customer.error.not_found")));
    }

    @GetMapping
    public Mono<String> getProductPage(@PathVariable("productId") int id, Model model) {
        return productReviewService.findAllProductReviewByProductId(id)
                .collectList()
                .doOnNext(productReviewDisplayDTO -> model.addAttribute("reviews", productReviewDisplayDTO))
                .then(this.favouriteProductService.findFavouriteProductById(id)
                        .doOnNext(favouriteProduct -> model.addAttribute("inFavourite", true)))
                .thenReturn("customer/products/product");
    }


    @PostMapping("/add-to-favourites")
    public Mono<String> addProductToFavourites(@ModelAttribute("product") Mono<Product> product) {
        return product
                .map(Product::getId)
                .flatMap(productId -> this.favouriteProductService.addProductToFavourite(productId)
                        .thenReturn("redirect:/customer/products/%s".formatted(productId))
                        .onErrorResume(exception -> {
                            log.error(exception.getMessage(), exception);
                            return Mono.just("redirect:/customer/products/%s".formatted(productId));
                        })
                );
    }

    @PostMapping("/delete-from-favourites")
    public Mono<String> deleteProductFavourites(@ModelAttribute("product") Mono<Product> product) {
        return product
                .map(Product::getId)
                .flatMap(productId -> this.favouriteProductService.removeProductToFavourite(productId)
                        .thenReturn("redirect:/customer/products/%s".formatted(productId))
                );
    }

    @PostMapping("/add-review")
    public Mono<String> addProductReview(@PathVariable("productId") int productId,
                                         ProductReviewCreateDTO dto,
                                         Model model) {
        return this.productReviewService.createProductReview(productId, dto)
                .thenReturn("redirect:/customer/products/%s".formatted(productId))
                .onErrorResume(ClientBadRequestException.class, exception -> {
                    model.addAttribute("inFavourite", false);
                    model.addAttribute("dto", dto);
                    model.addAttribute("errors", exception.getErrors());
                    return this.favouriteProductService.findFavouriteProductById(productId)
                            .doOnNext(favouriteProduct -> model.addAttribute("inFavourite", true))
                            .thenReturn("redirect:/customer/products/%s".formatted(productId));
                });
    }


    @ModelAttribute
    public Mono<CsrfToken> loadCsrfToken(ServerWebExchange exchange) {
        return exchange.<Mono<CsrfToken>>getAttribute(CsrfToken.class.getName())
                .doOnSuccess(csrfToken -> exchange.getAttributes()
                        .put(CsrfRequestDataValueProcessor.DEFAULT_CSRF_ATTR_NAME, csrfToken));

    }
}
