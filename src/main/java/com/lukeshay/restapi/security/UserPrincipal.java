package com.lukeshay.restapi.security;

import com.lukeshay.restapi.user.User;
import com.lukeshay.restapi.user.UserTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserPrincipal implements UserDetails, Principal {

	private User user;

	public UserPrincipal(User user) {
		this.user = user;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		String role = user.getRole();
		if (role == null || !role.equals(UserTypes.ADMIN.role())) {
			role = "BASIC_ROLE";
		}

		String authority = user.getAuthority();
		if (authority == null || !role.equals(UserTypes.ADMIN.authority())) {
			authority = "BASIC";
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(authority));
		authorities.add(new SimpleGrantedAuthority(role));
		return authorities;
	}

	@Override
	public String getName() {
		return user.getFirstName() + " " + user.getLastName();
	}

	@Override
	public String getPassword() {
		if (user == null) {
			return "";
		}
		return user.getPassword();
	}

	public User getUser() {
		return user;
	}

	@Override
	public String getUsername() {
		if (user == null) {
			return "";
		}
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserPrincipal that = (UserPrincipal) o;
		return Objects.equals(user, that.getUser());
	}

	@Override
	public int hashCode() {
		return Objects.hash(user);
	}
}
