package com.nastyastrel.springbootrest.service.user;

import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    private UserServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(userRepository);
    }

    @Test
    void itShouldFindAllUsers() {
        //given
        User user1 = new User(3L, "Filipp", "Kirkorov", "filipp");
        User user2 = new User(1L, "Nikolai", "Frolov", "nikola");
        List<User> users = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        //when
        var result = underTest.findAll();

        //then
        verify(userRepository).findAll();
        assertEquals(users, result);
    }

    @Test
    void itShouldSaveUser() {
        //given
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");

        //when
        underTest.save(user);

        //then
        verify(userRepository).save(user);
    }

    @Test
    void itShouldFindUserByLogin() {
        //given
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        //when
        var answer = underTest.findByLogin(user.getLogin());

        //then
        verify(userRepository).findByLogin(user.getLogin());
        assertEquals(Optional.of(user), answer);
    }

    @Test
    void name() {
        //given
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        MockedStatic<SecurityContextHolder> mockedStatic = Mockito.mockStatic(SecurityContextHolder.class);
        SecurityContext securityContext = new SecurityContextImpl();
        mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        Authentication authentication = securityContext.getAuthentication();
        System.out.println(authentication.getName());

        //when
        var answer = underTest.getAuthenticatedUser();

        //then
        assertEquals(Optional.of(user), answer);
    }
}