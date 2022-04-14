package com.ead.authuser.controllers;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserServices;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServices userServices;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() throws Exception{
        UserModel userModel = userRepository.save(obterDadosUser());
    }

    public UserModel obterDadosUser(){
        UserModel userModel = new UserModel();
        userModel.setUserId(UUID.fromString("9bbf4622-3725-47fc-ae31-7405265ccd7e"));
        userModel.setUsername("MockTest1");
        userModel.setEmail("email@email.com");
        userModel.setPassword("123456");
        userModel.setCpf("12345679");
        userModel.setImageUrl("imagem");
        userModel.setFullName("fullname");
        userModel.setPhoneNumber("1199458452");
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now());
        userModel.setLastUpdateDate(LocalDateTime.now());
        return userModel;
    }

    @After
    public final void tearDown(){
        userRepository.deleteAll();
    }


    /*@Test
    public void testGetAllUser() throws Exception {
        List<UserModel> listUsers = new ArrayList<>();
        listUsers.add(new UserModel());

        Mockito.when(userServices.findAll()).thenReturn(listUsers);

        String url = "/users";
       mockMvc
               .perform(MockMvcRequestBuilders.get(url))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }*/

    @Test
    public void testGetByUser() throws Exception {
        final List<UserModel> listUsers = new ArrayList<>();
        listUsers.add(new UserModel());

        Mockito.when(this.userServices.findById(UUID.fromString("9bbf4622-3725-47fc-ae31-7405265ccd7e")))
                .thenReturn(Optional.ofNullable(listUsers.get(0)));

        final String url = "/users/{userId}";
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(url,"9bbf4622-3725-47fc-ae31-7405265ccd7e"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}