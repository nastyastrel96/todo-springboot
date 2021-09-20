package com.nastyastrel.springbootrest.repository;

import com.nastyastrel.springbootrest.model.user.RoleName;
import com.nastyastrel.springbootrest.model.user.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}
