package com.lukeshay.restapi.user.bodys;

import com.google.gson.annotations.Expose;
import com.lukeshay.restapi.user.User;
import com.lukeshay.restapi.utils.ModelUtils;

public class NewUser extends User {
  @Expose private String recaptcha;

  NewUser() {}

  NewUser(User user, String recaptcha) {
    super(
        user.getUsername(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail(),
        user.getPhoneNumber(),
        user.getCity(),
        user.getState(),
        user.getCountry(),
        user.getPassword());
    this.recaptcha = recaptcha;
  }

  NewUser(
      String username,
      String firstName,
      String lastName,
      String email,
      String phoneNumber,
      String city,
      String state,
      String country,
      String password,
      String recaptcha) {
    super(username, firstName, lastName, email, phoneNumber, city, state, country, password);
    this.recaptcha = recaptcha;
  }

  public String getRecaptcha() {
    return recaptcha;
  }

  public void setRecaptcha(String recaptcha) {
    this.recaptcha = recaptcha;
  }

  @Override
  public String toString() {
    return ModelUtils.toString(this);
  }
}
