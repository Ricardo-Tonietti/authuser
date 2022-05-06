package com.ead.authuser.controllers;

import com.ead.authuser.configs.security.JwtProvider;
import com.ead.authuser.dtos.JwtDto;
import com.ead.authuser.dtos.LoginDto;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.RoleType;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.RoleModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.RoleService;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser (@RequestBody
                                                    @Validated(UserDto.UserView.RegistrationPost.class)
                                                    @JsonView (UserDto.UserView.RegistrationPost.class) final UserDto userDto){

        AuthenticationController.log.debug("POST :registerUser userDto received {} ", userDto.toString());
        if(userService.existsByUsername(userDto.getUsername())){
            AuthenticationController.log.warn("POST :username {} is Already Taken ", userDto.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: username is Already Taken!");
        }

        if(userService.existsByEmail(userDto.getEmail())){
            AuthenticationController.log.warn("POST :email {} is Already Taken ", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Email is Already Taken!");
        }
        RoleModel roleModel = roleService.findByRoleName(RoleType.ROLE_STUDENT)
                .orElseThrow( () -> new RuntimeException("ERROR: Role is Not Found."));

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.getRoles().add(roleModel);
        userService.saveUser(userModel);

        AuthenticationController.log.debug("POST :registerUser UserId saved {} ", userModel.getUserId());
        AuthenticationController.log.info("User saved sucessfully UserId {} ", userModel.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
    @PostMapping("/login")
    public ResponseEntity<JwtDto> authenticateUser(@Valid @RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwt(authentication);
        return ResponseEntity.ok(new JwtDto(jwt));
    }

    @GetMapping("/")
    public String index(){
        AuthenticationController.log.trace("TRACE");
        AuthenticationController.log.debug("DEBUG");
        AuthenticationController.log.info("INFO");
        AuthenticationController.log.warn("WARN");
        AuthenticationController.log.error("ERROR");
        return "Loggin Spring Boot....";
    }

    @PostMapping("/signup/admin/usr")
    public ResponseEntity<Object> registerUserAdmin (@RequestBody
                                                @Validated(UserDto.UserView.RegistrationPost.class)
                                                @JsonView (UserDto.UserView.RegistrationPost.class) final UserDto userDto){

        AuthenticationController.log.debug("POST :registerUser userDto received {} ", userDto.toString());
        if(userService.existsByUsername(userDto.getUsername())){
            AuthenticationController.log.warn("POST :username {} is Already Taken ", userDto.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: username is Already Taken!");
        }

        if(userService.existsByEmail(userDto.getEmail())){
            AuthenticationController.log.warn("POST :email {} is Already Taken ", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Email is Already Taken!");
        }
        RoleModel roleModel = roleService.findByRoleName(RoleType.ROLE_ADMIN)
                .orElseThrow( () -> new RuntimeException("ERROR: Role is Not Found."));

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.ADMIM);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.getRoles().add(roleModel);
        userService.saveUser(userModel);

        AuthenticationController.log.debug("POST :registerUser UserId saved {} ", userModel.getUserId());
        AuthenticationController.log.info("User saved sucessfully UserId {} ", userModel.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
}
