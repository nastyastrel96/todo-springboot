package com.nastyastrel.springbootrest.service.user;

import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> findAll() {
        List<User> todoItems = new ArrayList<>();
        repository.findAll().forEach(todoItems::add);
        return todoItems;
    }

    @Override
    public void save(User item) {
        repository.save(item);
    }

    @Override
    public Optional<User> findByLogin(String userName) {
        Optional<User> userOptional = repository.findByLogin(userName);
        if (userOptional.isPresent()) {
            return repository.findByLogin(userName);
        } else return userOptional;
    }

    @Override
    public User definePrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = null;
        if (findByLogin(authentication.getName()).isPresent()) {
            authenticatedUser = findByLogin(authentication.getName()).get();
        }
        return authenticatedUser;
    }
}
