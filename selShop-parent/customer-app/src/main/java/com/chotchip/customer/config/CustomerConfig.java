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
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CustomerConfig {

    @Bean
    @Scope("prototype")
    public WebClient.Builder servicesWebClientBuilder(ReactiveClientRegistrationRepository clientRegistrationRepository
            , ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        var serverOAuth2AuthorizedClientExchangeFilterFunction = new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository, authorizedClientRepository);
        serverOAuth2AuthorizedClientExchangeFilterFunction.setDefaultClientRegistrationId("keycloak");
        return WebClient.builder()
                .filter(serverOAuth2AuthorizedClientExchangeFilterFunction);
    }

    @Bean
    public ProductsClient productsClient(
            @Value("${selmag.services.catalogue.url:http://localhost:8081}") String baseURL,
            WebClient.Builder servicesWebClientBuilder) {
        return new ProductsClientWebClient(servicesWebClientBuilder
                .baseUrl(baseURL)
                .build());
    }

    @Bean
    public ProductReviewClient productReviewClient(
            @Value("${selmag.services.feedback.url:http://localhost:8084}") String baseURL,
            WebClient.Builder servicesWebClientBuilder) {
        return new ProductReviewClientWebClient(servicesWebClientBuilder
                .baseUrl(baseURL)
                .build());
    }

    @Bean
    public FavouriteProductClient favouriteProductClient(
            @Value("${selmag.services.feedback.url:http://localhost:8084}") String baseURL,
            WebClient.Builder servicesWebClientBuilder) {
        return new FavouriteProductClientWebClient(servicesWebClientBuilder
                .baseUrl(baseURL)
                .build());
    }


}
