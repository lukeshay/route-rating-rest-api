package com.lukeshay.restapi.security;

import com.lukeshay.restapi.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserPrincipalTest extends TestBase {

  @Test
  void hasCorrectAuthoritiesTest() {
    Assertions.assertEquals(2, userPrincipal.getAuthorities().size());

    boolean containsAuthority =
        userPrincipal
            .getAuthorities()
            .stream()
            .anyMatch(
                grantedAuthority -> grantedAuthority.getAuthority().equals(user.getAuthority()));
    boolean containsRole =
        userPrincipal
            .getAuthorities()
            .stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(user.getRole()));

    Assertions.assertAll(
        () -> Assertions.assertTrue(containsAuthority), () -> Assertions.assertTrue(containsRole));
  }

  @Test
  void getNameTest() {
    Assertions.assertEquals(
        user.getFirstName() + " " + user.getLastName(), userPrincipal.getName());
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
    Assertions.assertTrue(
        userPrincipal.isAccountNonExpired()
            && userPrincipal.isAccountNonExpired()
            && userPrincipal.isAccountNonLocked()
            && userPrincipal.isEnabled());
  }
}
