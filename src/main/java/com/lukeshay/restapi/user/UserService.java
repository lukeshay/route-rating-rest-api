package com.lukeshay.restapi.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface UserService {

	Logger LOG = LoggerFactory.getLogger(UserService.class.getName());

	ResponseEntity<?> createUser(User user, UserTypes type);

	User deleteUserById(String userId);

	Iterable<User> getAllUsers();

	User getUser(Authentication authentication);

	boolean isEmailTaken(Authentication authentication, String email);

	boolean isUsernameTaken(Authentication authentication, String username);

	User updateUser(
			Authentication authentication,
			String username,
			String email,
			String firstName,
			String lastName,
			String city,
			String state,
			String country,
			String password
	);

	/**
	 * Checks if email is valid, not null, and not in use. If it is invalid in anyway, a value with
	 * the key of `email` is added to the map.
	 *
	 * @param responseBody The current response body map
	 * @param email        The email of the user
	 * @return boolean of whether the email is valid
	 */
	boolean validateEmail(
			Map<String, String> responseBody, Authentication authentication, String email
	);

	/**
	 * Checks if the password is not null and valid format. If is in invalid in anyway, a value with
	 * the key of 'password' is added to the map.
	 *
	 * @param responseBody The current response body map
	 * @param password     The password of the user
	 * @return boolean of whether the password is valid
	 */
	boolean validatePassword(Map<String, String> responseBody, String password);

	/**
	 * Validates recaptcha by hitting Google's API endpoint.
	 *
	 * @param responseBody The current response body map
	 * @param recaptcha    The recaptcha string
	 * @return boolean of whether the recaptcha is valid
	 */
	boolean validateRecaptcha(Map<String, String> responseBody, String recaptcha);

	/**
	 * Validates state by matching it with regex.
	 *
	 * @param responseBody The current response body map
	 * @param state        The state string
	 * @return boolean of whether the state is valid
	 */
	boolean validateState(Map<String, String> responseBody, String state);

	/**
	 * Checks if the username is not null and not in use. If is in invalid in anyway, a value with the
	 * key of 'username' is added to the map.
	 *
	 * @param responseBody The current response body map
	 * @param username     The username of the user
	 * @return boolean of whether the username is valid
	 */
	boolean validateUsername(
			Map<String, String> responseBody, Authentication authentication, String username
	);
}
