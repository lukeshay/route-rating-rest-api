package com.lukeshay.restapi.user;

import com.lukeshay.restapi.TestBase;
import com.lukeshay.restapi.security.UserPrincipal;
import com.lukeshay.restapi.user.bodys.NewUser;
import com.lukeshay.restapi.utils.BodyUtils;
import com.lukeshay.restapi.utils.ResponseUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.RestTemplate;

class UserControllerTest extends TestBase {

  private UserController userController;
  @Mock private RestTemplateBuilder mockRestTemplateBuilder;
  @Mock private RestTemplate mockRestTemplate;

  @BeforeEach
  void setUp() {
    Map<String, Object> map = new HashMap<>();
    map.put("success", true);

    Mockito.when(
            mockRestTemplate.postForEntity(
                Mockito.anyString(), Mockito.anyMap(), Map.class, Mockito.anyMap()))
        .thenReturn(ResponseUtils.okOfType(map));
    Mockito.when(mockRestTemplateBuilder.build()).thenReturn(mockRestTemplate);

    userController =
        new UserController(
            new UserServiceImpl(userRepository, passwordEncoder, mockRestTemplateBuilder));
  }

  @Test
  void createUserTest() {
    authentication =
        new UsernamePasswordAuthenticationToken(
            new UserPrincipal(null), null, Collections.emptyList());
    User testUserTwo =
        new User(
            "TestUserTwo",
            "Test",
            "User",
            "test.user.two@email.com",
            "1111111111",
            "Des Moines",
            "Iowa",
            "USA",
            "password");
    testUserTwo.setLastName("User");

    NewUser newUser = new NewUser(testUserTwo, "asdf");

    ResponseEntity<?> getUser = userController.createUser(newUser);
    testUserTwo = userRepository.findByUsername(testUserTwo.getUsername()).get();

    Assertions.assertEquals(testUserTwo, getUser.getBody());

    testUser.setId(null);
    ResponseEntity<?> responseEmail = userController.createUser(testUser, "asdf");

    Assertions.assertAll(
        () -> Assertions.assertEquals(BodyUtils.error("Email taken."), responseEmail.getBody()),
        () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEmail.getStatusCode()));

    testUser.setEmail("testtest@email.com");

    ResponseEntity<?> responseUsername = userController.createUser(testUser, "asdf");

    Assertions.assertAll(
        () ->
            Assertions.assertEquals(BodyUtils.error("Username taken."), responseUsername.getBody()),
        () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseUsername.getStatusCode()));
  }

  @Test
  @WithMockUser
  void updateUserTest() {
    User testUserTwo =
        new User(
            "test.user2@email.com",
            "Test",
            "User",
            "test.user2@email.com",
            "1111111111",
            "Des Moines",
            "Iowa",
            "USA",
            "password");

    testUserTwo.setAuthority(UserTypes.BASIC.authority());
    testUserTwo.setRole(UserTypes.BASIC.role());

    testUserTwo = userRepository.save(testUserTwo);

    testUser.setUsername("TestUserChange");
    testUser.setFirstName("First");
    testUser.setLastName("Last");

    ResponseEntity<?> updatedUser = userController.updateUser(authentication, testUser);

    testUser = userRepository.findById(testUser.getId()).orElse(null);

    Assertions.assertAll(
        () -> Assertions.assertEquals(testUser, updatedUser.getBody()),
        () -> Assertions.assertEquals(HttpStatus.OK, updatedUser.getStatusCode()));

    testUser.setEmail(testUserTwo.getEmail());
    ResponseEntity<?> responseEmail = userController.createUser(testUser, "asdf");

    Assertions.assertAll(
        () -> Assertions.assertEquals(BodyUtils.error("Email taken."), responseEmail.getBody()),
        () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEmail.getStatusCode()));

    testUser.setEmail("testtest@email.com");
    testUser.setUsername(testUserTwo.getUsername());
    ResponseEntity<?> responseUsername = userController.createUser(testUser, "asdf");

    Assertions.assertAll(
        () ->
            Assertions.assertEquals(BodyUtils.error("Username taken."), responseUsername.getBody()),
        () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseUsername.getStatusCode()));
  }
}
