package com.financeapp.backend.services;

import com.financeapp.backend.enums.Roles;
import com.financeapp.backend.model.RoleModel;
import com.financeapp.backend.model.User;
import com.financeapp.backend.repository.RoleRepository;
import com.financeapp.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public void userDeletion(String email) {
        User deleteUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );
        userRepository.delete(deleteUser);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("User not found by email")
                );
    }

    public void addRoleToUser(String email, Roles role) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        RoleModel roleModel = roleRepository.findByRole(role)
                .orElseThrow(() -> new RuntimeException("Role not found: " + role));

        if (!user.getRoles().contains(roleModel)) {
            user.getRoles().add(roleModel);
            userRepository.save(user);
        }
    }
}
