package com.nastyastrel.springbootrest.repository;

import com.nastyastrel.springbootrest.model.user.Role;
import org.springframework.data.repository.CrudRepository;


public interface RoleRepository extends CrudRepository<Role, Integer> {
}
