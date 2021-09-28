package com.nastyastrel.springbootrest.restclient;

import com.nastyastrel.springbootrest.model.clientchucknorris.ChuckNorrisJoke;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

@Service
public class ChuckNorrisClient {
    private final RestOperations restOperations;

    public ChuckNorrisClient(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    public ChuckNorrisJoke getChuckNorrisJoke() {
        return restOperations.getForObject("https://api.chucknorris.io/jokes/random", ChuckNorrisJoke.class);
    }
}
