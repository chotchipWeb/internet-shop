package com.chotchip.customer.config;

import com.chotchip.customer.client.FavouriteProductClient;
import com.chotchip.customer.client.ProductReviewClient;
import com.chotchip.customer.client.ProductsClient;
import com.chotchip.customer.client.impl.FavouriteProductClientWebClient;
import com.chotchip.customer.client.impl.ProductReviewClientWebClient;
import com.chotchip.customer.client.impl.ProductsClientWebClient;
import de.codecentric.boot.admin.client.registration.ReactiveRegistrationClient;
import de.codecentric.boot.admin.client.registration.RegistrationClient;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.DefaultClientRequestObservationConvention;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class ClientBeans {

    //    Это для resourseServer

    @Bean
    public RegistrationClient registrationClient(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService
    ) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction filter =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                        new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository,
                                authorizedClientService));
        filter.setDefaultClientRegistrationId("metrics");

        return new ReactiveRegistrationClient(WebClient.builder()
                .filter(filter)
                .build(),
                Duration.ZERO);
    }

    @Bean
    @Scope("prototype")
    @LoadBalanced
    public WebClient.Builder selmagServicesWebClientBuilder(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository,
            ObservationRegistry observationRegistry
    ) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction filter =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository,
                        authorizedClientRepository);
        filter.setDefaultClientRegistrationId("keycloak");
        return WebClient.builder()
                .observationRegistry(observationRegistry)
                .observationConvention(new DefaultClientRequestObservationConvention())
                .filter(filter);
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
            WebClient.Builder servicesWebClientBuilder
    ) {
        return new ProductReviewClientWebClient(servicesWebClientBuilder
                .baseUrl(baseURL)
                .build());
    }

    @Bean
    public FavouriteProductClient favouriteProductClient(
            @Value("${selmag.services.feedback.url:http://localhost:8084}") String baseURL,
            WebClient.Builder servicesWebClientBuilder
    ) {
        return new FavouriteProductClientWebClient(servicesWebClientBuilder
                .baseUrl(baseURL)
                .build());
    }
}
