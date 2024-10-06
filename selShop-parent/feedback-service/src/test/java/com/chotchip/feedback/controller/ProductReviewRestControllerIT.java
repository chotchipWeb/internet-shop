package com.chotchip.feedback.controller;

import com.chotchip.feedback.entity.ProductReview;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@SpringBootTest
@AutoConfigureWebTestClient
class ProductReviewRestControllerIT {


    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ReactiveMongoTemplate template;

    @BeforeEach
    void setUp() {
        this.template.insertAll(List.of(
                new ProductReview(UUID.fromString("2c63d85c-d5d1-4f5a-a8ba-0c6d566c8186"), 1, 1, "details 1", "user-1"),
                new ProductReview(UUID.fromString("bf1da8b0-8d63-47b1-a272-6a8bd308b1eb"), 1, 2, "details 2", "user-2"),
                new ProductReview(UUID.fromString("fec89d38-423c-49ec-a7fb-5a8f4393722a"), 1, 3, "details 3", "user-3")
        )).blockLast();
    }

    @AfterEach
    void tearDown() {
        this.template.remove(ProductReview.class);
    }

    @Test
    void findAllProductReviewProductId_ResponseFluxProductReviewDisplayDTO() {
        // then
        this.webTestClient
                .mutateWith(mockJwt())
                .get()
                .uri("/feedback-api/products-review/by-product-id/1")
                .exchange()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody().json("""
                        [
                            { "rating": 1 ,"details": "details 1"},
                            { "rating": 2, "details": "details 2"},
                            { "rating": 3, "details": "details 3"}
                           
                        ]
                        """);

    }

    @Test
    void createProductReview_RequestIsValid_ResponseProductReview() {

        // when
        this.webTestClient
                .mutateWith(mockJwt().jwt(builder -> builder.subject("user-tester")))
                .post()
                .uri("/feedback-api/products-review/by-product-id/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                        "rating": 1,
                        "details": "details"
                        }
                                          """)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody()
                .json("""
                                        {
                                        "productId": 1,
                                        "rating": 1,
                                        "details": "details",
                                        "userId": "user-tester"
                                        }
                        """).jsonPath("$.id").exists();

    }

    @Test
    void createProductReview_RequestIsInValid_ResponseProductReview() {

        // when
        this.webTestClient
                .mutateWith(mockJwt().jwt(builder -> builder.subject("user-tester")))
                .post()
                .uri("/feedback-api/products-review/by-product-id/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                        "rating": null,
                        "details": "details"
                        }
                                          """)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody()
                .json("""
                                        {
                                        "errors": [
                                        "рейтинг не может быть равен нулю "
                                        ]
                                        }
                        """);

    }

    @Test
    void createProductReview_RequestIsNotAuthorized_ResponseProductReview() {

        // when
        this.webTestClient
                .post()
                .uri("/feedback-api/products-review/by-product-id/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                        "rating": 1,
                        "details": "details"
                        }
                                          """)
                .exchange()
                .expectStatus().isUnauthorized();


    }

}