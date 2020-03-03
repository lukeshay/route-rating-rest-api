package com.lukeshay.restapi.session;

import io.jsonwebtoken.Claims;

public interface SessionService {

  Session createSession(
      String jwtToken,
      Claims jwtClaims,
      Long expiresIn,
      String refreshToken,
      Claims refreshClaims,
      String userId);

  void deleteSession(Session session);

  Session saveSession(Session session);
}
