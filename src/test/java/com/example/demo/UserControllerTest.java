package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        Cart cart = new Cart();
        User user = new User();
        user.setId(0);
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setCart(cart);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
    }

    @Test
    public void createUserTest(){
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0,user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void findByUserNameTest(){

        ResponseEntity<User> userResponseEntity = userController.findByUserName("testUser");
        assertNotNull(userResponseEntity);
        assertEquals(200,userResponseEntity.getStatusCodeValue());

        User user = userResponseEntity.getBody();

        assertEquals(0,user.getId());
        assertEquals("testUser",user.getUsername());
        assertEquals("testPassword",user.getPassword());

    }

    @Test
    public void findUserByIdTest(){
        ResponseEntity<User> user = userController.findById(1L);
        assertEquals(404,user.getStatusCodeValue());
    }




}
