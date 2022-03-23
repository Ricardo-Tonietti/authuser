package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserServices;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    public static final String userNotFound = "User Not Found";
    @Autowired
    UserServices userServices;

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers( SpecificationTemplate.UserSpec spec,
                                            @PageableDefault(page = 0, size = 5,
            sort = "userId", direction = Sort.Direction.ASC)Pageable pageable){

        Page<UserModel> userModelPage = userServices.findAll(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserId (@PathVariable("userId") final UUID userId){
        final Optional<UserModel> userModelOptional = this.userServices.findById(userId);

        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserController.userNotFound);
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserId (@PathVariable("userId") final UUID userId){
        final Optional<UserModel> userModelOptional = this.userServices.findById(userId);

        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserController.userNotFound);
        }else {
            this.userServices.delete(userModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("User deleted success");
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser (@PathVariable("userId") final UUID userId,
                                              @RequestBody @Validated(UserDto.UserView.UserPut.class)
                                              @JsonView(UserDto.UserView.UserPut.class) final UserDto userDto){

        final Optional<UserModel> userModelOptional = this.userServices.findById(userId);

        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserController.userNotFound);
        }else {
            final var userModel = userModelOptional.get();
            userModel.setFullName(userDto.getFullName());
            userModel.setPhoneNumber(userDto.getPhoneNumber());
            userModel.setCpf(userDto.getCpf());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

            this.userServices.save(userModel);
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword (@PathVariable("userId") final UUID userId,
                                                  @RequestBody @Validated(UserDto.UserView.PasswordPut.class)
                                                  @JsonView(UserDto.UserView.PasswordPut.class) final UserDto userDto){

        final Optional<UserModel> userModelOptional = this.userServices.findById(userId);

        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserController.userNotFound);
        } else if(!userModelOptional.get().getPassword().equals(userDto.getOldPassword())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password!");
        }
        else {
            final var userModel = userModelOptional.get();
            userModel.setPassword(userDto.getPassword());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

            this.userServices.save(userModel);
            return ResponseEntity.status(HttpStatus.OK).body("Password updates successfully");
        }
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage (@PathVariable("userId") final UUID userId,
                                               @RequestBody @Validated(UserDto.UserView.ImagePut.class)
                                               @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto){

        final Optional<UserModel> userModelOptional = this.userServices.findById(userId);

        if(!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserController.userNotFound);
        }else {
            final var userModel = userModelOptional.get();
            userModel.setImageUrl(userDto.getImageUrl());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

            this.userServices.save(userModel);
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }
}
