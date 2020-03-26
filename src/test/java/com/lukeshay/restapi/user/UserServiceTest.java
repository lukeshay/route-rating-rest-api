package com.lukeshay.restapi.user;

import com.lukeshay.restapi.TestBase;
import com.lukeshay.restapi.utils.BodyUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class UserServiceTest extends TestBase {

	private UserService userService;
	@Mock
	private RecaptchaValidator recaptchaValidator;

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
		ResponseEntity<?> createValidUser = userService.createUser(user, UserTypes.BASIC);

		user = userRepository.findAll().get(0);

		Assertions.assertAll(
			() -> Assertions.assertEquals(user, createValidUser.getBody()),
			() ->
				Assertions.assertEquals(
					UserTypes.BASIC.authority(), ((User) createValidUser.getBody()).getAuthority()),
			() -> Assertions.assertEquals(HttpStatus.OK, createValidUser.getStatusCode()));

		userRepository.deleteAll();
		ResponseEntity<?> createValidAdminUser = userService.createUser(user, UserTypes.ADMIN);

		user = userRepository.findAll().get(0);

		Assertions.assertAll(
			() -> Assertions.assertEquals(user, createValidAdminUser.getBody()),
			() ->
				Assertions.assertEquals(
					UserTypes.ADMIN.authority(),
					((User) createValidAdminUser.getBody()).getAuthority()),
			() -> Assertions.assertEquals(HttpStatus.OK, createValidAdminUser.getStatusCode()));

		userRepository.deleteAll();

		user.setId(null);
		user.setUsername(null);

		ResponseEntity<?> createInvalidUser = userService.createUser(user, UserTypes.BASIC);

		Assertions.assertAll(
			() ->
				Assertions.assertEquals(
					BodyUtils.error("Field missing for user."), createInvalidUser.getBody()),
			() -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, createInvalidUser.getStatusCode()));
	}

	@Test
	void deleteUserByIdTest() {
		user = userRepository.save(user);
		User deletedUser = userService.deleteUserById(user.getId());
		Assertions.assertEquals(user, deletedUser);

		deletedUser = userService.deleteUserById(user.getId());
		Assertions.assertNull(deletedUser);
	}

	//  TODO(Luke)
	//  @Test
	//  void getAllUsersTest() {}

	@Test
	void getUserTest() {
		User getUser = userService.getUser(authentication);
		Assertions.assertEquals(user, getUser);

		getUser = userService.getUser(null);
		Assertions.assertNull(getUser);
	}

	@Test
	void isEmailTakenTest() {
		user = userRepository.save(user);

		boolean emailTaken = userService.isEmailTaken(null, user.getEmail());
		Assertions.assertTrue(emailTaken);

		emailTaken = userService.isEmailTaken(authentication, user.getEmail());
		Assertions.assertFalse(emailTaken);

		emailTaken = userService.isEmailTaken(null, "luke@shay.com");
		Assertions.assertFalse(emailTaken);
	}
}
