package com.lukeshay.restapi.security;

import java.util.Objects;

public class CredentialsPayload {

	private String username;
	private String password;
	private Boolean remember;

	public CredentialsPayload() {
	}

	public CredentialsPayload(String username, String password, Boolean remember) {
		this.username = username;
		this.password = password;
		this.remember = remember;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getRemember() {
		return remember;
	}

	public void setRemember(Boolean remember) {
		this.remember = remember;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		CredentialsPayload that = (CredentialsPayload) o;
		return Objects.equals(username, that.username)
			&& Objects.equals(password, that.password)
			&& Objects.equals(remember, that.remember);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username, password, remember);
	}
}
