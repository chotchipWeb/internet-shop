package com.chotchip.manager.controller;

import com.chotchip.manager.client.impl.RestClientProductImpl;
import com.chotchip.manager.dto.ProductCreateDTO;
import com.chotchip.manager.entity.Product;
import com.chotchip.manager.exception.BadRequestException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("standalone")
@WireMockTest(httpPort = 54321)
class ProductsControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Mock
    RestClientProductImpl restClientProduct;

    @Test
    void getNewProductPage_ReturnsProductPage() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/catalogue/products/create")
                .with(user("ura").roles("MANAGER"));
        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("catalogue/products/create")

                );
    }

    @Test
    void createProduct_ResponseRedirectListPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/catalogue/products/create")
                .param("title", "Новый товар")
                .param("details", "Описание нового товара")
                .with(user("ura1").roles("MANAGER"))
                .with(csrf());

        //when
        mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().is3xxRedirection()

                );
    }

    @Test
    void getProductList_ResponseRedirectListPage() throws Exception {

        // given
        var requestBuilder = MockMvcRequestBuilders.get("/catalogue/products/list")
                .with(user("ura1").roles("MANAGER"));

        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/catalogue-api/products"))
                .willReturn(WireMock.ok("""
                                [
                                    {"id": 1, "title": "title", "details": "details"}
                                ]
                                   """)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));


        //when
        this.mockMvc.perform(requestBuilder)

                // then
                .andDo(print())
                .andExpectAll(
                        status().is3xxRedirection()
                );
    }

}