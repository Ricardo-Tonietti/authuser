
package com.ead.authuser.repositories;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    final String userId = "c51afe7b-d3f1-4edd-beec-f0132244ac21";
    final String username = "username";

    @Before
    public void setUp() throws Exception{
        final UserModel userModel = this.userRepository.save(this.obterDadosUser());
    }

    public UserModel obterDadosUser(){
        final UserModel userModel = new UserModel();
        userModel.setUserId(UUID.fromString(this.userId));
        userModel.setUsername(this.username);
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
        this.userRepository.deleteAll();
    }

    @Test
    public void testExistsByUsername() {
        assertEquals(this.userRepository.existsByUsername("username"),true);
    }

    @Test
    public void testExistsByEmail() {
        assertEquals(this.userRepository.existsByEmail("email@email.com"),true);
    }

    @Test
    public void testFindById(){
        final Optional<UserModel> userModelOptional = this.userRepository.findById(UUID.fromString(this.userId));

        if(userModelOptional.isPresent()){
            assertEquals(userModelOptional, this.userId);
        }
    }

    @Test
    public void testNotFoundUser(){
        final Optional<UserModel> userModelOptional = this.userRepository.findById(UUID.randomUUID());

        if(!userModelOptional.isPresent()){
            assertNotNull(userModelOptional);
        }
    }

    @Test
    public void testListAllUser(){
        final List<UserModel> userModel = this.userRepository.findAll();

        assertTrue(String.valueOf(userModel.isEmpty()),true);

        assertEquals(1,userModel.size());
    }
}
