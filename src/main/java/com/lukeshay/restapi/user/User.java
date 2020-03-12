package com.lukeshay.restapi.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.google.gson.annotations.Expose;
import com.lukeshay.restapi.utils.Auditable;
import com.lukeshay.restapi.utils.ModelUtils;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "users")
public class User extends Auditable<String> {

  @Column(name = "id", unique = true, updatable = false, nullable = false)
  @Expose
  @GeneratedValue(generator = "pg-uuid")
  @GenericGenerator(name = "pg-uuid", strategy = "org.hibernate.id.UUIDGenerator")
  @Id
  private String id;

  @Column(name = "password")
  @JsonProperty(access = Access.WRITE_ONLY)
  private String password;

  @Column(name = "username", unique = true, nullable = false)
  @Expose
  private String username;

  @Column(name = "email", unique = true, nullable = false)
  @Expose
  private String email;

  @Column(name = "first_name", nullable = false)
  @Expose
  private String firstName;

  @Column(name = "last_name", nullable = false)
  @Expose
  private String lastName;

  @Column(name = "phone_number", length = 10, nullable = false)
  @Expose
  private String phoneNumber;

  @Column(name = "city", nullable = false)
  @Expose
  private String city;

  @Column(name = "state", nullable = false)
  @Expose
  private String state;

  @Column(name = "country", nullable = false)
  @Expose
  private String country;

  @Column(name = "authority", length = 5, nullable = false)
  @Expose
  private String authority;

  @Column(name = "role", length = 10, nullable = false)
  @Expose
  private String role;

  public User() {}

  public User(
      String username,
      String firstName,
      String lastName,
      String email,
      String phoneNumber,
      String city,
      String state,
      String country,
      String password) {
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.city = city;
    this.state = state;
    this.country = country;
    this.password = password;
  }

  public User(
      String id,
      String username,
      String firstName,
      String lastName,
      String email,
      String phoneNumber,
      String city,
      String state,
      String country,
      String password,
      String authority,
      String role) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.city = city;
    this.state = state;
    this.country = country;
    this.password = password;
    this.authority = authority;
    this.role = role;
  }

  //  @Override
  //  public boolean equals(Object obj) {
  //    if (obj == null) {
  //      return false;
  //    }
  //    if (obj == this) {
  //      return true;
  //    }
  //    if (obj.getClass() != getClass()) {
  //      return false;
  //    }
  //    User rhs = (User) obj;
  //
  //    return super.equals(obj)
  //        && this.id.equals(rhs.id)
  //        && this.password.equals(rhs.password)
  //        && this.username.equals(rhs.username)
  //        && this.email.equals(rhs.email)
  //        && this.firstName.equals(rhs.firstName)
  //        && this.lastName.equals(rhs.lastName)
  //        && this.phoneNumber.equals(rhs.phoneNumber)
  //        && this.city.equals(rhs.city)
  //        && this.state.equals(rhs.state)
  //        && this.country.equals(rhs.country)
  //        && this.authority.equals(rhs.authority)
  //        && this.role.equals(rhs.role);
  //  }

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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
    User user = (User) o;
    return Objects.equals(id, user.id)
        && Objects.equals(password, user.password)
        && Objects.equals(username, user.username)
        && Objects.equals(email, user.email)
        && Objects.equals(firstName, user.firstName)
        && Objects.equals(lastName, user.lastName)
        && Objects.equals(phoneNumber, user.phoneNumber)
        && Objects.equals(city, user.city)
        && Objects.equals(state, user.state)
        && Objects.equals(country, user.country)
        && Objects.equals(authority, user.authority)
        && Objects.equals(role, user.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        password,
        username,
        email,
        firstName,
        lastName,
        phoneNumber,
        city,
        state,
        country,
        authority,
        role);
  }
}
