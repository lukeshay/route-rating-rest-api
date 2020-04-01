package com.lukeshay.restapi.user;

import com.lukeshay.restapi.TestBase;
import com.lukeshay.restapi.security.UserPrincipal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class UserControllerTest extends TestBase {

	private UserController userController;
	@Mock private RecaptchaValidator recaptchaValidator;

	@BeforeEach
	void setUp() {
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);

		Mockito.when(recaptchaValidator.validate(Mockito.anyString())).thenReturn(true);

		userController = new UserController(new UserServiceImpl(userRepository, passwordEncoder, recaptchaValidator));
	}

	@Test
	void createUserTest() {
		authentication =
				new UsernamePasswordAuthenticationToken(new UserPrincipal(null), null, Collections.emptyList());
		User testUserTwo = new User(
				"TestUserTwo",
				"Test",
				"User",
				"test.user.two@email.com",
				"1111111111",
				"Des Moines",
				"Iowa",
				"USA",
				"password"
		);
		testUserTwo.setLastName("User");

		NewUserBody newUserBody = new NewUserBody(testUserTwo, "asdf");

		ResponseEntity<?> getUser = userController.createUser(newUserBody);
		testUserTwo = userRepository.findByUsername(testUserTwo.getUsername()).get();

		Assertions.assertEquals(testUserTwo, getUser.getBody());

		user.setId(null);
		user.setUsername(UUID.randomUUID().toString());
		newUserBody = new NewUserBody(user, "asdf");
		ResponseEntity<?> responseEmail = userController.createUser(newUserBody);

		Map<String, Object> map = new HashMap<>();

		map.put("email", "Email is already in use.");

		Assertions.assertAll(
				() -> Assertions.assertEquals(map, responseEmail.getBody()),
				() -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEmail.getStatusCode())
		);

		user.setEmail("testtest@email.com");
		user.setUsername(testUserTwo.getUsername());
		newUserBody = new NewUserBody(user, "asdf");

		ResponseEntity<?> responseUsername = userController.createUser(newUserBody);

		map.clear();
		map.put("username", "Username is already in use.");

		Assertions.assertAll(
				() -> Assertions.assertEquals(map, responseUsername.getBody()),
				() -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseUsername.getStatusCode())
		);
	}

	@Test
	@WithMockUser
	void updateUserTest() {
		User testUserTwo = new User(
				"test.user2@email.com",
				"Test",
				"User",
				"test.user2@email.com",
				"1111111111",
				"Des Moines",
				"Iowa",
				"USA",
				"password"
		);

		testUserTwo.setAuthority(UserTypes.BASIC.authority());
		testUserTwo.setRole(UserTypes.BASIC.role());

		testUserTwo = userRepository.save(testUserTwo);

		user.setUsername("TestUserChange");
		user.setFirstName("First");
		user.setLastName("Last");

		ResponseEntity<?> updatedUser = userController.updateUser(authentication, user);

		user = userRepository.findById(user.getId()).orElse(null);

		Assertions.assertAll(
				() -> Assertions.assertEquals(user, updatedUser.getBody()),
				() -> Assertions.assertEquals(HttpStatus.OK, updatedUser.getStatusCode())
		);

		user.setEmail(testUserTwo.getEmail());
		ResponseEntity<?> responseEmail = userController.updateUser(authentication, user);

		Map<String, String> map = new HashMap<>();

		map.put("email", "Email is already in use.");

		Assertions.assertAll(
				() -> Assertions.assertEquals(map, responseEmail.getBody()),
				() -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEmail.getStatusCode())
		);

		user.setEmail("testtest@email.com");
		user.setUsername(testUserTwo.getUsername());
		ResponseEntity<?> responseUsername = userController.updateUser(authentication, user);

		map.clear();
		map.put("username", "Username is already in use.");

		Assertions.assertAll(
				() -> Assertions.assertEquals(map, responseUsername.getBody()),
				() -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseUsername.getStatusCode())
		);
	}
}
