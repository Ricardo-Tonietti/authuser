package com.ead.authuser.services.impl;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    final String userId = "c51afe7b-d3f1-4edd-beec-f0132244ac21";

    @Before
    public void setUp() throws Exception{
        BDDMockito.given(userRepository.findById(UUID.fromString(this.userId)))
                .willReturn(Optional.of(new UserModel()));
    }

    @Test
    public void testFindById() {
        final Optional<UserModel> usuer = userService.findById(UUID.fromString(this.userId));
        assertTrue(usuer.isPresent());
    }


}