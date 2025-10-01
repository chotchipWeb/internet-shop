package com.chotchip.manager.config;

import com.chotchip.manager.client.RestClientProduct;
import com.chotchip.manager.client.impl.RestClientProductImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

import static org.mockito.Mockito.mock;


@TestConfiguration
class TestingBeans {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return mock(ClientRegistrationRepository.class);
    }

    @Bean
    public OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository() {
        return mock(OAuth2AuthorizedClientRepository.class);
    }

    @Bean
    public RestClientProductImpl restClientConfig(
            @Value("${selMag.service.catalogue.uri:http://localhost:54321}") String catalogueBaseUri
    ) {
        return new RestClientProductImpl(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .build()) {
        };
    }
}
