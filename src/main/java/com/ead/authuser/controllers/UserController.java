package com.ead.authuser.controllers;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    UserServices userServices;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userServices.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserId (@PathVariable(value = "userId") UUID userId){
        Optional<UserModel> userModelOptional = userServices.findById(userId);

        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserId (@PathVariable(value = "userId") UUID userId){
        Optional<UserModel> userModelOptional = userServices.findById(userId);

        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }else {
            userServices.delete(userModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("User deleted success");
        }
    }


}
