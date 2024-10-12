//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.cloud.netflix.eureka.http;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.cloud.netflix.eureka.RestTemplateTimeoutProperties;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.lang.Nullable;

public class DefaultEurekaClientHttpRequestFactorySupplier implements EurekaClientHttpRequestFactorySupplier {
    private final RestTemplateTimeoutProperties restTemplateTimeoutProperties;
    private final List<HttpRequestInterceptor> interceptors;

    /**
     * @deprecated
     */
    @Deprecated
    public DefaultEurekaClientHttpRequestFactorySupplier() {
        this.restTemplateTimeoutProperties = new RestTemplateTimeoutProperties();
        this.interceptors = List.of();
    }

    public DefaultEurekaClientHttpRequestFactorySupplier(RestTemplateTimeoutProperties restTemplateTimeoutProperties) {
        this.restTemplateTimeoutProperties = restTemplateTimeoutProperties;
        this.interceptors = List.of();
    }

    public DefaultEurekaClientHttpRequestFactorySupplier(RestTemplateTimeoutProperties restTemplateTimeoutProperties, List<HttpRequestInterceptor> interceptors) {
        this.restTemplateTimeoutProperties = restTemplateTimeoutProperties;
        this.interceptors = interceptors;
    }

    public ClientHttpRequestFactory get(SSLContext sslContext, @Nullable HostnameVerifier hostnameVerifier) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        if (sslContext != null || hostnameVerifier != null || this.restTemplateTimeoutProperties != null) {
            httpClientBuilder.setConnectionManager(this.buildConnectionManager(sslContext, hostnameVerifier, this.restTemplateTimeoutProperties));
        }

        if (this.restTemplateTimeoutProperties != null) {
            httpClientBuilder.setDefaultRequestConfig(this.buildRequestConfig());
        }
        this.interceptors.forEach(httpClientBuilder::addRequestInterceptorLast);
        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return requestFactory;
    }

    private HttpClientConnectionManager buildConnectionManager(SSLContext sslContext, HostnameVerifier hostnameVerifier, RestTemplateTimeoutProperties restTemplateTimeoutProperties) {
        PoolingHttpClientConnectionManagerBuilder connectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create();
        SSLConnectionSocketFactoryBuilder sslConnectionSocketFactoryBuilder = SSLConnectionSocketFactoryBuilder.create();
        if (sslContext != null) {
            sslConnectionSocketFactoryBuilder.setSslContext(sslContext);
        }

        if (hostnameVerifier != null) {
            sslConnectionSocketFactoryBuilder.setHostnameVerifier(hostnameVerifier);
        }

        connectionManagerBuilder.setSSLSocketFactory(sslConnectionSocketFactoryBuilder.build());
        if (restTemplateTimeoutProperties != null) {
            connectionManagerBuilder.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(Timeout.of((long) restTemplateTimeoutProperties.getSocketTimeout(), TimeUnit.MILLISECONDS)).build());
        }

        return connectionManagerBuilder.build();
    }

    private RequestConfig buildRequestConfig() {
        return RequestConfig.custom().setConnectTimeout(Timeout.of((long) this.restTemplateTimeoutProperties.getConnectTimeout(), TimeUnit.MILLISECONDS)).setConnectionRequestTimeout(Timeout.of((long) this.restTemplateTimeoutProperties.getConnectRequestTimeout(), TimeUnit.MILLISECONDS)).build();
    }
}