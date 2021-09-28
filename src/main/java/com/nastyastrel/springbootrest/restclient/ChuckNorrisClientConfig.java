package com.nastyastrel.springbootrest.restclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ChuckNorrisClientConfig {
    @Bean
    public RestOperations restOperations() {
        return new RestTemplate();
    }
}
