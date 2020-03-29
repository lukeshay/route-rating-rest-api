package com.lukeshay.restapi.security;

import com.lukeshay.restapi.TestBase;
import com.lukeshay.restapi.user.UserTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserPrincipalTest extends TestBase {

	@Test
	void hasCorrectAuthoritiesTest() {
		Assertions.assertEquals(2, userPrincipal.getAuthorities().size());

		boolean containsAuthority = userPrincipal.getAuthorities()
		                                         .stream()
		                                         .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
		                                                                                       .equals(user.getAuthority()));
		boolean containsRole = userPrincipal.getAuthorities()
		                                    .stream()
		                                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
		                                                                                  .equals(user.getRole()));

		Assertions.assertAll(() -> Assertions.assertTrue(containsAuthority), () -> Assertions.assertTrue(containsRole));
	}

	@Test
	void hasInvalidAuthoritiesTest() {
		user.setAuthority(null);
		user.setRole(null);
		userPrincipal = new UserPrincipal(user);

		Assertions.assertEquals(2, userPrincipal.getAuthorities().size());

		boolean containsAuthorityFromNull = userPrincipal.getAuthorities()
		                                                 .stream()
		                                                 .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
		                                                                                               .equals(UserTypes.BASIC
				                                                                                               .authority()));
		boolean containsRoleFromNull = userPrincipal.getAuthorities()
		                                            .stream()
		                                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
		                                                                                          .equals(UserTypes.BASIC
				                                                                                          .role()));

		Assertions.assertAll(
				() -> Assertions.assertTrue(containsAuthorityFromNull),
				() -> Assertions.assertTrue(containsRoleFromNull)
		);

		user.setAuthority(null);
		user.setRole(null);
		userPrincipal = new UserPrincipal(user);

		Assertions.assertEquals(2, userPrincipal.getAuthorities().size());

		boolean containsAuthorityFromInvalid = userPrincipal.getAuthorities()
		                                                    .stream()
		                                                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
		                                                                                                  .equals(UserTypes.BASIC
				                                                                                                  .authority()));
		boolean containsRoleFromInvalid = userPrincipal.getAuthorities()
		                                               .stream()
		                                               .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
		                                                                                             .equals(UserTypes.BASIC
				                                                                                             .role()));

		Assertions.assertAll(
				() -> Assertions.assertTrue(containsAuthorityFromInvalid),
				() -> Assertions.assertTrue(containsRoleFromInvalid)
		);
	}

	@Test
	void getNameTest() {
		Assertions.assertEquals(user.getFirstName() + " " + user.getLastName(), userPrincipal.getName());
	}

	@Test
	void getPasswordTest() {
		Assertions.assertEquals(user.getPassword(), userPrincipal.getPassword());
	}

	@Test
	void getUsernameTest() {
		Assertions.assertEquals(user.getUsername(), userPrincipal.getUsername());
	}

	@Test
	void getUnusedMethodsTest() {
		Assertions.assertTrue(userPrincipal.isAccountNonExpired() && userPrincipal.isAccountNonExpired() && userPrincipal
				.isAccountNonLocked() && userPrincipal.isEnabled());
	}
}
