package com.financeapp.backend.controller;

import com.financeapp.backend.enums.Roles;
import com.financeapp.backend.model.User;
import com.financeapp.backend.services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticateUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User currentUser = userService.findByEmail(email);

        return ResponseEntity.ok(currentUser);
    }
    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List<User> user = userService.allUsers();
        return ResponseEntity.ok(user);
    }

    // TODO: add so users can remove accounts based on JWT token



    //delete for admin-role TODO -> add roles with spring security
    @DeleteMapping("/{email}")
    public ResponseEntity<String> removeUser(@PathVariable String email) {
        userService.userDeletion(email);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/add-role")
    public ResponseEntity<String> addRoles(@RequestParam String email, @RequestParam Roles role) {
        userService.addRoleToUser(email, role);
        return ResponseEntity.ok("Role added.");
    }


}
