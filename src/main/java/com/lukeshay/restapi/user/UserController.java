package com.lukeshay.restapi.user;

import com.lukeshay.restapi.user.bodys.NewUser;
import com.lukeshay.restapi.utils.BodyUtils;
import com.lukeshay.restapi.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
@Api(value = "User api endpoints.")
public class UserController {

  private static Logger LOG = LoggerFactory.getLogger(UserController.class.getName());

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("")
  @PreAuthorize("isAuthenticated()")
  @ApiOperation(value = "Get a user by email.", response = User.class)
  public ResponseEntity<?> getUser(Authentication authentication) {
    LOG.debug("Getting user.");

    User user = userService.getUser(authentication);

    if (user == null) {
      LOG.debug("Could not find user");
      return ResponseUtils.notFound(BodyUtils.error("User not found."));
    } else {
      return ResponseUtils.ok(user);
    }
  }

  @PutMapping("")
  @PreAuthorize("isAuthenticated()")
  @ApiOperation(value = "Update a user.", response = User.class)
  public ResponseEntity<?> updateUser(Authentication authentication, @RequestBody User user) {

    LOG.debug("Updating user.");

    ResponseEntity<?> response =
        checkDuplicate(authentication, user.getEmail(), user.getUsername());

    if (response != null) {
      return response;
    }

    user =
        userService.updateUser(
            authentication,
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getCity(),
            user.getState(),
            user.getCountry(),
            user.getPassword());

    if (user == null) {
      LOG.debug("User was not found");

      return ResponseUtils.badRequest(BodyUtils.error("User not found."));
    } else {
      return ResponseUtils.ok(user);
    }
  }

  @PostMapping("/admin")
  @PreAuthorize("hasAuthority('ADMIN')")
  @ApiIgnore
  public ResponseEntity<?> createAdminUser(Authentication authentication, @RequestBody User body) {
    LOG.debug("Creating user {}", body.toString());

    Map<String, String> responseBody = new HashMap<>();

    boolean validEmail = userService.validateNewUserEmail(responseBody, body.getEmail());
    boolean validUsername = userService.validateNewUserUsername(responseBody, body.getUsername());
    boolean validPassword = userService.validateNewUserPassword(responseBody, body.getPassword());

    if (!validEmail || !validUsername || !validPassword) {
      return ResponseUtils.badRequest(responseBody);
    }

    return userService.createUser(body, UserTypes.ADMIN);
  }

  @DeleteMapping("/{userId}")
  @PreAuthorize("hasAuthority('ADMIN')")
  @ApiIgnore
  public ResponseEntity<?> deleteUserByUserId(@PathVariable String userId) {
    User deletedUser = userService.deleteUserById(userId);

    if (deletedUser == null) {
      return ResponseUtils.badRequest(BodyUtils.error("User not found."));
    } else {
      return ResponseUtils.ok(deletedUser);
    }
  }

  @PostMapping("/new")
  @PreAuthorize("permitAll()")
  @ApiOperation(value = "Create a user.", response = User.class)
  public ResponseEntity<?> createUser(@RequestBody NewUser body) {
    LOG.debug("Creating user {}", body.toString());

    Map<String, String> responseBody = new HashMap<>();

    boolean validEmail = userService.validateNewUserEmail(responseBody, body.getEmail());
    boolean validUsername = userService.validateNewUserUsername(responseBody, body.getUsername());
    boolean validPassword = userService.validateNewUserPassword(responseBody, body.getPassword());
    boolean validRecaptcha = userService.validateRecaptcha(responseBody, body.getRecaptcha());

    if (!validEmail || !validUsername || !validPassword || !validRecaptcha) {
      return ResponseUtils.badRequest(responseBody);
    }

    return userService.createUser(body, UserTypes.BASIC);
  }

  @GetMapping("/all")
  @PreAuthorize("hasAuthority('ADMIN')")
  @ApiIgnore
  public ResponseEntity<?> getAllUsers() {
    LOG.debug("Getting all users.");

    Iterable<User> users = userService.getAllUsers();

    return ResponseUtils.ok(users);
  }

  private ResponseEntity<?> checkDuplicate(
      Authentication authentication, String email, String username) {

    if (userService.isEmailTaken(authentication, email)) {
      LOG.debug("Not creating user because email is taken");

      return ResponseUtils.badRequest(BodyUtils.error("Email taken."));
    }

    if (userService.isUsernameTaken(authentication, username)) {
      LOG.debug("Not creating user because email is taken");

      return ResponseUtils.badRequest(BodyUtils.error("Username taken."));
    }

    return null;
  }
}
