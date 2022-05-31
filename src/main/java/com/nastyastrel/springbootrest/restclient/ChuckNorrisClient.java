package com.nastyastrel.springbootrest.restclient;

import com.nastyastrel.springbootrest.model.chucknorris.ChuckNorrisJoke;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

@Service
@AllArgsConstructor
public class ChuckNorrisClient {
    private final RestOperations restOperations;

    public ChuckNorrisJoke getChuckNorrisJoke() {
        return restOperations.getForObject("https://api.chucknorris.io/jokes/random", ChuckNorrisJoke.class);
    }
}
