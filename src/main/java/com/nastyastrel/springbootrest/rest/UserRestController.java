package com.nastyastrel.springbootrest.rest;

import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserRestController {
    private final UserService userService;

    @GetMapping("/users")
    public List<User> findAll() {
        return userService.findAll();
    }

}
