package com.lukeshay.restapi.user;

public enum UserTypes {
  BASIC,
  ADMIN;

  public String authority() {
    return this.toString();
  }

  public boolean equals(String role) {
    return this.role().equals(role);
  }

  public String role() {
    return this.toString() + "_ROLE";
  }
}
