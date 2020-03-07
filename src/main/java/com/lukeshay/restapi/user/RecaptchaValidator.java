package com.lukeshay.restapi.user;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RecaptchaValidator {

  private static final String GOOGLE_RECAPTCHA_VERIFY_URL =
      "https://www.google.com/recaptcha/api/siteverify";
  private static final String GOOGLE_RECAPTCHA_TOKEN = System.getenv("GOOGLE_RECAPTCHA_TOKEN");
  private final RestTemplateBuilder restTemplateBuilder;
  Logger LOG = LoggerFactory.getLogger(RecaptchaValidator.class.getName());
  private Map lastResponse;

  @Autowired
  public RecaptchaValidator(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplateBuilder = restTemplateBuilder;
  }

  public String getRecaptchaToken() {
    return GOOGLE_RECAPTCHA_TOKEN;
  }

  public boolean validate(String recaptcha) {
    Map<String, String> body = new HashMap<>();
    body.put("secret", GOOGLE_RECAPTCHA_TOKEN);
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

    LOG.debug("Recaptcha response: {}", lastResponse);

    if (lastResponse == null) {
      return false;
    }

    return (Boolean) lastResponse.get("success");
  }
}
