package com.chotchip.customer.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;


@SpringBootTest
@AutoConfigureWebTestClient
@WireMockTest(httpPort = 54321)
class ProductControllerIT {

    @Autowired
    WebTestClient webTestClient;


    @Test
    void addProductToFavourites_RequestIsValid_ResponseRedirectProductPage() {
        // given
        WireMock.stubFor(WireMock.get("/catalogue-api/products/1")
                .willReturn(WireMock.okJson("""
                        {
                        "id": 1,
                        "title": "title",
                        "details": "details"
                        }
                        """).withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        WireMock.stubFor(WireMock.post("/feedback-api/favourites-products/by-product-id/1")
                .willReturn(WireMock.created()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                {
                                    "uuid": "37fd97ff-ce80-485b-a0d4-0d56643e78cc",
                                    "productId": 1
                                }""")));
        // when
        this.webTestClient
                .mutateWith(mockUser())
                .mutateWith(csrf())
                .post()
                .uri("/customer/products/1/add-to-favourites")
                // then
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/customer/products/1");


        WireMock.verify(WireMock.getRequestedFor(WireMock.urlPathMatching("/catalogue-api/products/1")));
        WireMock.verify(WireMock.postRequestedFor(WireMock.urlPathMatching("/feedback-api/favourites-products/by-product-id/1")));

    }
}