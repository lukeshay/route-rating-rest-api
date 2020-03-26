package com.lukeshay.restapi.security;

import com.lukeshay.restapi.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserPrincipalServiceTest extends TestBase {
	@Autowired
	private UserPrincipalService userPrincipalService;

	@Test
	void loadUserByUsernameTest() {
		UserPrincipal validUsername =
			(UserPrincipal) userPrincipalService.loadUserByUsername(user.getUsername());
		UserPrincipal invalidUsername =
			(UserPrincipal) userPrincipalService.loadUserByUsername("ADJFLDKFJ");

		Assertions.assertAll(
			() -> Assertions.assertNotNull(validUsername),
			() -> Assertions.assertEquals(user, validUsername.getUser()),
			() -> Assertions.assertEquals(userPrincipal, validUsername),
			() -> Assertions.assertNull(invalidUsername.getUser()));
	}
}
