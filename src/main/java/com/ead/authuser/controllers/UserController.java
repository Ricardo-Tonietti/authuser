package com.ead.authuser.controllers;

import com.ead.authuser.configs.security.AuthenticationCurrentUserService;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    public static final String userNotFound = "User Not Found";
    @Autowired
    UserService userService;

    @Autowired
    AuthenticationCurrentUserService authenticationCurrentUserService;

    @PreAuthorize("hasAnyRole('INSTRUCTOR' )")
    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(final SpecificationTemplate.UserSpec spec,
                                                       @PageableDefault(page = 0, size = 5, sort = "userId",
                                                   direction = Sort.Direction.ASC) final Pageable pageable,
                                                       Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("Authentication {}", userDetails.getUsername());

        Page<UserModel> userModelPage = this.userService.findAll(spec, pageable);

        if (!userModelPage.isEmpty()) {
            for (final UserModel user : userModelPage.toList()) {
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable("userId") final UUID userId) {
        UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
        if(currentUserId.equals(userId)){
            Optional<UserModel> userModelOptional = userService.findById(userId);

            if (!userModelOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFound);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
            }
        }else {
            throw new AccessDeniedException("Forbidden");
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserId(@PathVariable("userId") UUID userId) {
        UserController.log.debug("DELETE deleteUser userId received {} ", userId);

        final Optional<UserModel> userModelOptional = userService.findById(userId);
        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFound);
        } else {
            userService.deleteUser(userModelOptional.get());
            UserController.log.debug("DELETE deleteUser userId deleted {} ", userId);
            UserController.log.info("User deleted successfully userId {} ", userId);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted success");
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable("userId") UUID userId,
                                             @RequestBody @Validated(UserDto.UserView.UserPut.class)
                                             @JsonView(UserDto.UserView.UserPut.class) UserDto userDto) {

        UserController.log.debug("PUT updateUser userDto received {} ", userDto.toString());
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFound);
        } else {
            final var userModel = userModelOptional.get();
            userModel.setFullName(userDto.getFullName());
            userModel.setPhoneNumber(userDto.getPhoneNumber());
            userModel.setCpf(userDto.getCpf());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

            userService.updateUser(userModel);
            UserController.log.debug("PUT updateUser getUserId saved {} ", userModel.getUserId());
            UserController.log.info("User saved sucessfully {} ", userModel.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable("userId") UUID userId,
                                              @RequestBody @Validated(UserDto.UserView.ImagePut.class)
                                              @JsonView(UserDto.UserView.ImagePut.class) final UserDto userDto) {

        UserController.log.debug("PUT updateImage userDto received {} ", userDto.toString());
        final Optional<UserModel> userModelOptional = userService.findById(userId);

        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFound);
        } else {
            final var userModel = userModelOptional.get();
            userModel.setImageUrl(userDto.getImageUrl());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

            userService.updateUser(userModel);
            UserController.log.debug("PUT updateUser getUserId saved {} ", userModel.getUserId());
            UserController.log.info("User saved sucessfully {} ", userModel.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }
    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable("userId") UUID userId,
                                                 @RequestBody @Validated(UserDto.UserView.PasswordPut.class)
                                                 @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto) {

        UserController.log.debug("PUT updatePassword userDto received {} ", userDto.toString());
        final Optional<UserModel> userModelOptional = userService.findById(userId);

        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFound);
        } else if (!userModelOptional.get().getPassword().equals(userDto.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password!");
        } else {
            final var userModel = userModelOptional.get();
            userModel.setPassword(userDto.getPassword());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

            userService.updatePassword(userModel);
            UserController.log.debug("PUT updateUser getUserId saved {} ", userModel.getUserId());
            UserController.log.info("User saved sucessfully {} ", userModel.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body("Password updates successfully");
        }
    }

}
