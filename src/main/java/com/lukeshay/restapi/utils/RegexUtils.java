package com.lukeshay.restapi.utils;

public class RegexUtils {

  public static String VALID_EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
  public static String VALID_PASSWORD_REGEX = "";

  public static boolean isValidEmail(String email) {
    return email.matches(VALID_EMAIL_REGEX);
  }

  public static boolean isValidPassword(String password) {
    return password.matches(VALID_PASSWORD_REGEX);
  }
}
