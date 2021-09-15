package com.nastyastrel.springbootrest.restclient;

import com.nastyastrel.springbootrest.model.clientchucknorris.ChuckNorrisJoke;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class ChuckNorrisClient {
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public ChuckNorrisJoke getChuckNorrisJoke() {
        return restTemplate().getForObject("https://api.chucknorris.io/jokes/random", ChuckNorrisJoke.class);
    }
}
