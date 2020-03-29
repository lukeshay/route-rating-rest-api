package com.lukeshay.restapi.user.bodys;

import com.google.gson.annotations.Expose;
import com.lukeshay.restapi.user.User;
import com.lukeshay.restapi.utils.ModelUtils;

public class NewUser extends User {

	@Expose private String recaptcha;

	NewUser() {
	}

	public NewUser(User user, String recaptcha) {
		super(
				user.getUsername(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getPhoneNumber(),
				user.getCity(),
				user.getState(),
				user.getCountry(),
				user.getPassword()
		);
		this.recaptcha = recaptcha;
	}

	public String getRecaptcha() {
		return recaptcha;
	}

	public void setRecaptcha(String recaptcha) {
		this.recaptcha = recaptcha;
	}

	public User getUser() {
		return new User(
				getEmail(),
				getUsername(),
				getFirstName(),
				getLastName(),
				getEmail(),
				getPhoneNumber(),
				getCity(),
				getState(),
				getCountry(),
				getPassword(),
				getAuthority(),
				getRole()
		);
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
