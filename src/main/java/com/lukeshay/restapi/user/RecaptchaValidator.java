package com.lukeshay.restapi.user;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RecaptchaValidator {

  private static final String GOOGLE_RECAPTCHA_VERIFY_URL =
      "https://www.google.com/recaptcha/api/siteverify";

  private final RestTemplateBuilder restTemplateBuilder;

  private Map<String, Object> lastResponse;

  @Value("{google.recaptcha.token}")
  private String recaptchaToken;

  @Autowired
  public RecaptchaValidator(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplateBuilder = restTemplateBuilder;
  }

  public boolean validate(String recaptcha) {
    Map<String, String> body = new HashMap<>();
    body.put("secret", recaptchaToken);
    body.put("response", recaptcha);

    ResponseEntity<Map> recaptchaResponseEntity =
        restTemplateBuilder
            .build()
            .postForEntity(
                GOOGLE_RECAPTCHA_VERIFY_URL + "?secret={secret}&response={response}",
                body,
                Map.class,
                body);

    lastResponse = recaptchaResponseEntity.getBody();

    if (lastResponse == null) {
      return false;
    }

    return (Boolean) lastResponse.get("success");
  }

}
