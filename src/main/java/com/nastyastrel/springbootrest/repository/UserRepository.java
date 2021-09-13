package com.nastyastrel.springbootrest.repository;

import com.nastyastrel.springbootrest.model.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByLogin(String userName);
}
