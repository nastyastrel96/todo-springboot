package com.nastyastrel.springbootrest.service.user;

import com.nastyastrel.springbootrest.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    void save(User user);

    Optional<User> findByLogin(String login);

    Optional<User> getAuthenticatedUser();
}
