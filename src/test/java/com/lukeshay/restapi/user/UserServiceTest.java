package com.lukeshay.restapi.user;

import com.lukeshay.restapi.TestBase;
import com.lukeshay.restapi.utils.BodyUtils;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserServiceTest extends TestBase {

  private UserService userService;
  @Mock private RecaptchaValidator recaptchaValidator;

  @BeforeEach
  void setUp() {
    Map<String, Object> map = new HashMap<>();
    map.put("success", true);

    Mockito.when(recaptchaValidator.validate(Mockito.anyString())).thenReturn(true);

    userService = new UserServiceImpl(userRepository, passwordEncoder, recaptchaValidator);

    userRepository.deleteAll();
  }

  /*
  updateUser
  validateEmail
  validateUsername
  validatePassword
  validateRecaptcha
  validateState
       */

  @Test
  void createUserTest() {
    ResponseEntity<?> createValidUser = userService.createUser(testUser, UserTypes.BASIC);

    testUser = userRepository.findAll().get(0);

    Assertions.assertAll(
        () -> Assertions.assertEquals(testUser, createValidUser.getBody()),
        () ->
            Assertions.assertEquals(
                UserTypes.BASIC.authority(), ((User) createValidUser.getBody()).getAuthority()),
        () -> Assertions.assertEquals(HttpStatus.OK, createValidUser.getStatusCode()));

    userRepository.deleteAll();
    ResponseEntity<?> createValidAdminUser = userService.createUser(testUser, UserTypes.ADMIN);

    testUser = userRepository.findAll().get(0);

    Assertions.assertAll(
        () -> Assertions.assertEquals(testUser, createValidAdminUser.getBody()),
        () ->
            Assertions.assertEquals(
                UserTypes.ADMIN.authority(),
                ((User) createValidAdminUser.getBody()).getAuthority()),
        () -> Assertions.assertEquals(HttpStatus.OK, createValidAdminUser.getStatusCode()));

    userRepository.deleteAll();

    testUser.setId(null);
    testUser.setUsername(null);

    ResponseEntity<?> createInvalidUser = userService.createUser(testUser, UserTypes.BASIC);

    Assertions.assertAll(
        () ->
            Assertions.assertEquals(
                BodyUtils.error("Field missing for user."), createInvalidUser.getBody()),
        () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, createInvalidUser.getStatusCode()));
  }

  @Test
  void deleteUserByIdTest() {
    testUser = userRepository.save(testUser);
    User deletedUser = userService.deleteUserById(testUser.getId());
    Assertions.assertEquals(testUser, deletedUser);

    deletedUser = userService.deleteUserById(testUser.getId());
    Assertions.assertNull(deletedUser);
  }

  //  TODO(Luke)
  //  @Test
  //  void getAllUsersTest() {}

  @Test
  void getUserTest() {
    User getUser = userService.getUser(authentication);
    Assertions.assertEquals(testUser, getUser);

    getUser = userService.getUser(null);
    Assertions.assertNull(getUser);
  }

  @Test
  void isEmailTakenTest() {
    testUser = userRepository.save(testUser);

    boolean emailTaken = userService.isEmailTaken(null, testUser.getEmail());
    Assertions.assertTrue(emailTaken);

    emailTaken = userService.isEmailTaken(authentication, testUser.getEmail());
    Assertions.assertFalse(emailTaken);

    emailTaken = userService.isEmailTaken(null, "luke@shay.com");
    Assertions.assertFalse(emailTaken);
  }
}
