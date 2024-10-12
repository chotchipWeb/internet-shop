package com.chotchip.manager.config;

import com.chotchip.manager.client.impl.RestClientProductImpl;
import com.chotchip.manager.security.OAuthClientHttpRequestInterceptor;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClientProductImpl restClientProduct(
            @Value("${selMag.service.catalogue.uri:http://localhost:8081}") String catalogueBaseUri,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository,
            @Value("${selMag.service.catalogue.registration-id:keycloak}") String registrationId,
            LoadBalancerClient loadBalancerClient
    ) {
        return new RestClientProductImpl(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .requestInterceptor(new LoadBalancerInterceptor(loadBalancerClient))
                .requestInterceptor(new OAuthClientHttpRequestInterceptor(
                        new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository),
                        registrationId
                ))
                .build());
    }
}
