package com.financeapp.backend.tests;

import com.financeapp.backend.enums.Roles;
import com.financeapp.backend.model.RoleModel;
import com.financeapp.backend.model.User;
import com.financeapp.backend.repository.RoleRepository;
import com.financeapp.backend.repository.UserRepository;
import com.financeapp.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class userServiceTest {


    @Mock
    UserRepository users;
    @Mock
    RoleRepository role;

    UserService sut;

    @BeforeEach
    void setup() {
        sut = new UserService(users, role);
    }

    @Test
    void shouldReturnAllUsers() {
        User u1 = new User();
        User u2 = new User();
        Iterable<User> iterate = List.of(u1, u2);
        when(users.findAll()).thenReturn(iterate);
        List<User> result = sut.allUsers();

        assertEquals(2, result.size());
        assertTrue(result.contains(u1));
        assertTrue(result.contains(u2));
        verify(users).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() {
        Iterable<User> emptyUser = List.of();
        when(users.findAll()).thenReturn(emptyUser);
        List<User> result = sut.allUsers();
        assertTrue(result.isEmpty());
        verify(users).findAll();
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(users.findByEmail("zwukii@gmail.com")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
           sut.userDeletion("zwukii@gmail.com");
        });

        verify(users, never()).delete(any());

    }

    @Test
    void shouldDeleteExistingUser() {
        User userF = new User();
        when(users.findByEmail("fake@gmail.com")).thenReturn(Optional.of(userF));
        sut.userDeletion("fake@gmail.com");
        verify(users).delete(userF);
    }

    @Test
    void  shouldThrowExceptionWhenUserNotFoundByEmail() {
        when(users.findByEmail("zwukii@gmail.com")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
           sut.findByEmail("zwukii@gmail.com");
        });
        verify(users).findByEmail("zwukii@gmail.com");
    }

    @Test
    void shouldReturnUserWhenEmailExists() {
        User userF = new User();
        when(users.findByEmail("zwukii@gmail.com")).thenReturn(Optional.of(userF));
        User result = sut.findByEmail("zwukii@gmail.com");
        assertEquals(userF, result);
        verify(users).findByEmail("zwukii@gmail.com");

    }

    @Test
    void shouldThrowExceptionWhenUserNotAdded() {
        when(users.findByEmail("zwukii@gmail.com")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            sut.addRoleToUser("zwukii@gmail.com", Roles.ADMIN);
        });
        verify(users).findByEmail("zwukii@gmail.com");
    }

    @Test
    void shouldThrowExceptionWhenRoleNotFound() {
        User userF = new User();
        userF.setRoles(new ArrayList<>());

        when(users.findByEmail("zwukii@gmail.com")).thenReturn(Optional.of(userF));
        when(role.findByRole(Roles.ADMIN)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            sut.addRoleToUser("zwukii@gmail.com", Roles.ADMIN);
        });

        verify(role).findByRole(Roles.ADMIN);
        verify(users, never()).save(any());
    }

    @Test
    void shouldAddRoleToUserWhenNotPresent() {
        User user = new User();
        user.setRoles(new ArrayList<>());
        RoleModel admin = new RoleModel();

        when(users.findByEmail("zwukii")).thenReturn(Optional.of(user));
        when(role.findByRole(Roles.ADMIN)).thenReturn(Optional.of(admin));
        sut.addRoleToUser("zwukii", Roles.ADMIN);
        assertTrue(user.getRoles().contains(admin));
        verify(users).save(user);
    }

    @Test
     void shouldNotAddRoleWhenAlreadyPresent() {
        User user = new User();
        RoleModel admin = new RoleModel();
        user.setRoles(new ArrayList<>(List.of(admin)));
        when(users.findByEmail("zwukii")).thenReturn(Optional.of(user));
        when(role.findByRole(Roles.ADMIN)).thenReturn(Optional.of(admin));
        sut.addRoleToUser("zwukii", Roles.ADMIN);
        assertEquals(1, user.getRoles().size());
        verify(users, never()).save(any());

    }

}
