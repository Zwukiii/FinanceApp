package com.financeapp.backend.services;

import com.financeapp.backend.enums.Roles;
import com.financeapp.backend.model.RoleModel;
import com.financeapp.backend.repository.RoleRepository;
import com.financeapp.backend.repository.UserRepository;
import org.springframework.stereotype.Service;



@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;


    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;

    }

    public RoleModel createRole(Roles role) {
        return roleRepository.findByRole(role)
                .orElseGet( () -> roleRepository.save(new RoleModel(0L, role)));
    }

    public RoleModel findRoles(Roles role) {
        return roleRepository.findByRole(role)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public RoleModel deleteRole(long id) {
        if (!roleRepository.existsById(id)) {
            throw  new RuntimeException("Role doesnt exist");
        }

        RoleModel roles = roleRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Roles doesnt exist")
                );

        roleRepository.delete(roles);
        return  roles;
    }
}
