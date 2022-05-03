package com.ead.authuser.services.impl;

import com.ead.authuser.enums.RoleType;
import com.ead.authuser.models.RoleModel;
import com.ead.authuser.repositories.RoleRepository;
import com.ead.authuser.services.RoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    public RoleServiceImpl(final RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RoleModel> findByRoleName(RoleType roleType) {
        return repository.findByRoleName(roleType);
    }
}
