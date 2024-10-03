package com.chotchip.customer.config;

import com.chotchip.customer.client.ProductsClient;
import com.chotchip.customer.client.impl.ProductsClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CustomerConfig {

    @Bean
    public ProductsClient productsClient(
            @Value("${selmag.services.catalogue.url:http://localhost:8081}") String baseURL) {
        return new ProductsClientImpl(WebClient.builder()
                .baseUrl(baseURL)
                .build());
    }

}
