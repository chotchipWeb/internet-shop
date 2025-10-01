package com.chotchip.customer.config;

import com.chotchip.customer.client.FavouriteProductClient;
import com.chotchip.customer.client.ProductReviewClient;
import com.chotchip.customer.client.ProductsClient;
import com.chotchip.customer.client.impl.FavouriteProductClientWebClient;
import com.chotchip.customer.client.impl.ProductReviewClientWebClient;
import com.chotchip.customer.client.impl.ProductsClientWebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {


    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
        return mock();
    }

    @Bean
    public ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
        return mock();
    }

    @Bean
    @Primary
    public ProductsClient testProductsClient(
    ) {
        return new ProductsClientWebClient(WebClient.builder()
                .baseUrl("http://localhost:54321")
                .build());
    }

    @Bean
    @Primary
    public ProductReviewClient testProductReviewClient(
    ) {
        return new ProductReviewClientWebClient(WebClient.builder()
                .baseUrl("http://localhost:54321")
                .build());
    }

    @Bean
    @Primary
    public FavouriteProductClient testFavouriteProductClient(
    ) {
        return new FavouriteProductClientWebClient(WebClient.builder()
                .baseUrl("http://localhost:54321")
                .build());
    }
}
