package com.lukeshay.restapi.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RecaptchaValidator {
	private static final String GOOGLE_RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
	private final RestTemplateBuilder restTemplateBuilder;
	Logger LOG = LoggerFactory.getLogger(RecaptchaValidator.class.getName());
	@Value("${google.recaptcha.token}") private String googleRecaptchaToken;
	private Map lastResponse;

	@Autowired
	public RecaptchaValidator(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplateBuilder = restTemplateBuilder;
	}

	public boolean validate(String recaptcha) {
		Map<String, String> body = new HashMap<>();
		body.put("secret", googleRecaptchaToken);
		body.put("response", recaptcha);

		ResponseEntity<Map> recaptchaResponseEntity = restTemplateBuilder.build()
		                                                                 .postForEntity(GOOGLE_RECAPTCHA_VERIFY_URL + "?secret={secret}&response={response}",
				                                                                 body,
				                                                                 Map.class,
				                                                                 body
		                                                                 );

		lastResponse = recaptchaResponseEntity.getBody();

		LOG.debug("Recaptcha response: {}", lastResponse);

		if (lastResponse == null) {
			return false;
		}

		return (Boolean) lastResponse.get("success");
	}
}
