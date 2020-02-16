package com.lukeshay.restapi.user;

import com.lukeshay.restapi.utils.AuthenticationUtils;
import com.lukeshay.restapi.utils.BodyUtils;
import com.lukeshay.restapi.utils.RegexUtils;
import com.lukeshay.restapi.utils.ResponseUtils;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class UserServiceImpl implements UserService {

  private static final String GOOGLE_RECAPTCHA_VERIFY_URL =
      "https://www.google.com/recaptcha/api/siteverify";
  private static final String GOOGLE_RECAPTCHA_TOKEN = System.getenv("GOOGLE_RECAPTCHA_TOKEN");

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RestTemplateBuilder restTemplateBuilder;

  @Autowired
  UserServiceImpl(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      RestTemplateBuilder restTemplateBuilder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.restTemplateBuilder = restTemplateBuilder;
  }

  @Override
  public ResponseEntity<?> createUser(User user, UserTypes type) {
    if (user.getUsername() != null
        && user.getFirstName() != null
        && user.getLastName() != null
        && user.getEmail() != null
        && user.getPhoneNumber() != null
        && user.getCity() != null
        && user.getState() != null
        && user.getCountry() != null
        && user.getPassword() != null) {

      user.setPassword(passwordEncoder.encode(user.getPassword()));
      user.setAuthority(type.authority());
      user.setRole(type.role());

      LOG.debug("Creating {} user: {}", type, user);

      return ResponseUtils.ok(userRepository.save(user));

    } else {
      LOG.debug("Could not create {} user: {}", type, user);
      return ResponseUtils.badRequest(BodyUtils.error("Field missing for user."));
    }
  }

  @Override
  public User deleteUserById(String id) {
    User deletedUser = userRepository.findById(id).orElse(null);

    if (deletedUser == null) {
      return null;
    } else {
      userRepository.deleteById(id);
      return deletedUser;
    }
  }

  @Override
  public User getUser(Authentication authentication) {
    return AuthenticationUtils.getUser(authentication);
  }

  @Override
  public Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public boolean isEmailTaken(Authentication authentication, String email) {
    User user = AuthenticationUtils.getUser(authentication);

    return (user == null || !user.getEmail().equals(email))
        && userRepository.findByEmail(email).orElse(null) != null;
  }

  @Override
  public boolean isUsernameTaken(Authentication authentication, String username) {
    User user = AuthenticationUtils.getUser(authentication);

    return (user == null || !user.getUsername().equals(username))
        && userRepository.findByUsername(username).orElse(null) != null;
  }

  @Override
  public User updateUser(
      Authentication authentication,
      String username,
      String email,
      String firstName,
      String lastName,
      String city,
      String state,
      String country,
      String password) {

    User user = AuthenticationUtils.getUser(authentication);

    assert user != null;
    User toUpdate = userRepository.findById(user.getId()).orElse(null);

    if (toUpdate == null) {
      return null;
    }

    if (username != null && !username.equals("")) {
      toUpdate.setUsername(username);
    }

    if (email != null && !email.equals("")) {
      toUpdate.setEmail(email);
    }

    if (firstName != null && !firstName.equals("")) {
      toUpdate.setFirstName(firstName);
    }

    if (lastName != null && !lastName.equals("")) {
      toUpdate.setLastName(lastName);
    }

    if (city != null && !city.equals("")) {
      toUpdate.setCity(city);
    }

    if (state != null && !state.equals("")) {
      toUpdate.setState(state);
    }

    if (country != null && !country.equals("")) {
      toUpdate.setCountry(country);
    }

    if (password != null && !password.equals("")) {
      toUpdate.setPassword(passwordEncoder.encode(password));
    }

    return userRepository.save(toUpdate);
  }

  @Override
  public boolean validateNewUserEmail(Map<String, String> responseBody, String email) {
    if (email == null) {
      responseBody.put("email", "Email must be provided.");
      return false;
    }

    if (isEmailTaken(null, email)) {
      responseBody.put("email", "Email is already in use.");
      return false;
    }

    if (!RegexUtils.isValidEmail(email)) {
      responseBody.put("email", "Email is an incorrect format.");
      return false;
    }

    return true;
  }

  @Override
  public boolean validateNewUserUsername(Map<String, String> responseBody, String username) {
    if (username == null) {
      responseBody.put("username", "Username must be provided.");
      return false;
    }

    if (isUsernameTaken(null, username)) {
      responseBody.put("username", "Username is already in use.");
      return false;
    }

    return true;
  }

  @Override
  public boolean validateNewUserPassword(Map<String, String> responseBody, String password) {
    if (password == null) {
      responseBody.put("password", "Password must be provided.");
      return false;
    }

    if (!RegexUtils.isValidPassword(password)) {
      responseBody.put("password", "Password is an incorrect format.");
      return false;
    }

    return true;
  }

  @Override
  public boolean validateRecaptcha(Map<String, String> responseBody, String recaptcha) {
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

    LOG.debug("Response from recaptcha: {}", recaptchaResponseEntity);

    Map<String, Object> recaptchaResponseBody = recaptchaResponseEntity.getBody();

    return (Boolean) recaptchaResponseBody.get("success");
  }
}
