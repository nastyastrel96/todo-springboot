package com.nastyastrel.springbootrest.service.user;

import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "users")
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    @Cacheable(key = "#login", unless = "#result == null")
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public User getAuthenticatedUser(Principal principal) {
        return userRepository.findByLogin(principal.getName()).get();
    }
}
