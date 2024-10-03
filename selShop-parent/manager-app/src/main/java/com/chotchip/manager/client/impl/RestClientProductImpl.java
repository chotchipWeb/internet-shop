package com.chotchip.manager.client.impl;

import com.chotchip.manager.client.RestClientProduct;
import com.chotchip.manager.dto.ProductCreateDTO;
import com.chotchip.manager.dto.ProductUpdateDTO;
import com.chotchip.manager.entity.Product;
import com.chotchip.manager.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class RestClientProductImpl implements RestClientProduct {
    private final static ParameterizedTypeReference<Iterable<Product>> PARAMETERIZED_TYPE_REFERENCE = new ParameterizedTypeReference<>() {
    };

    private final RestClient restClient;

    @Override
    public Iterable<Product> findAllProducts() {
        return restClient.get()
                .uri("/catalogue-api/products")
                .retrieve()
                .body(PARAMETERIZED_TYPE_REFERENCE);
    }

    @Override
    public Product createProduct(ProductCreateDTO productCreateDTO) {
        try {
            return restClient.post()
                    .uri("/catalogue-api/products")
                    .body(productCreateDTO)
                    .retrieve()
                    .body(Product.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail responseBodyAs = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) responseBodyAs.getProperties().get("errors"));
        }

    }

    @Override
    public Optional<Product> findById(Integer id) {
        try {
            return Optional.ofNullable(this.restClient
                    .get()
                    .uri("/catalogue-api/products/{productId}", id)
                    .retrieve()
                    .body(Product.class)
            );
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }

    }

    @Override
    public void updateProduct(Integer id, ProductUpdateDTO productUpdateDTO) {
        try {
            this.restClient.put()
                    .uri("/catalogue-api/products/{productId}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(productUpdateDTO)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail responseBodyAs = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) responseBodyAs.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteProduct(Integer id) {
        try {
            this.restClient
                    .delete()
                    .uri("/catalogue-api/products/{productId}", id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NoSuchElementException(exception);
        }
    }
}
