package com.miu.ea.goodpeople.config;

import com.miu.ea.goodpeople.client.ProductClient;
import com.miu.ea.goodpeople.client.RequestClient;
import com.miu.ea.goodpeople.client.TripClient;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public TripClient tripClient() {
        return Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(TripClient.class))
            .logLevel(Logger.Level.FULL)
            .target(TripClient.class, "http://localhost:8081/api");
    }

    @Bean
    public RequestClient requestClient() {
        return Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(RequestClient.class))
            .logLevel(Logger.Level.FULL)
            .target(RequestClient.class, "http://localhost:8082/api");
    }

    @Bean
    public ProductClient productClient() {
        return Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(ProductClient.class))
            .logLevel(Logger.Level.FULL)
            .target(ProductClient.class, "http://localhost:8083/api/v1/products");
    }
}
