package com.lukeshay.restapi.security;

import com.google.gson.annotations.Expose;
import com.lukeshay.restapi.session.Session;
import com.lukeshay.restapi.user.User;
import com.lukeshay.restapi.utils.ModelUtils;
import java.util.Objects;

public class AuthBody {

  @Expose private User user;
  @Expose private Session session;

  public AuthBody(User user, Session session) {
    this.user = user;
    this.session = session;
  }

  @Override
  public String toString() {
    return ModelUtils.toString(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthBody authBody = (AuthBody) o;
    return Objects.equals(user, authBody.user) && Objects.equals(session, authBody.session);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, session);
  }
}
