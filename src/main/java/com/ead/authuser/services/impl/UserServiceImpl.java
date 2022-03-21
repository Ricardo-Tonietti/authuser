package com.ead.authuser.services.impl;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserServices {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<UserModel> findAll() {

        return this.userRepository.findAll();
    }

    @Override
    public Optional<UserModel> findById(final UUID userId) {
        return this.userRepository.findById(userId);
    }

    @Override
    public void delete(final UserModel userModel) {
        this.userRepository.delete(userModel);
    }

    @Override
    public void save(final UserModel userModel) {
        this.userRepository.save(userModel);
    }

    @Override
    public boolean existsByUsername(final String userName) {
        return this.userRepository.existsByUsername(userName);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return this.userRepository.existsByEmail(email);
    }
}
