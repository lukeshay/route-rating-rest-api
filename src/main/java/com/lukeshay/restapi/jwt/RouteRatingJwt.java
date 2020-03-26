package com.lukeshay.restapi.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.google.gson.annotations.Expose;
import com.lukeshay.restapi.utils.ModelUtils;
import io.jsonwebtoken.Claims;

import java.io.Serializable;
import java.util.Objects;

public class RouteRatingJwt implements Serializable {

	@Expose
	private String jwtToken;

	@JsonProperty(access = Access.WRITE_ONLY)
	private Claims jwtClaims;

	@Expose
	private Long expiresIn;
	@Expose
	private String refreshToken;

	@JsonProperty(access = Access.WRITE_ONLY)
	private Claims refreshClaims;

	public RouteRatingJwt(
		String jwtToken,
		Claims jwtClaims,
		Long expiresIn,
		String refreshToken,
		Claims refreshClaims) {
		this.jwtToken = jwtToken;
		this.jwtClaims = jwtClaims;
		this.expiresIn = expiresIn;
		this.refreshToken = refreshToken;
		this.refreshClaims = refreshClaims;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public Claims getJwtClaims() {
		return jwtClaims;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public Claims getRefreshClaims() {
		return refreshClaims;
	}

	public String getRefreshToken() {
		return refreshToken;
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
		RouteRatingJwt that = (RouteRatingJwt) o;
		return Objects.equals(jwtToken, that.jwtToken)
			&& Objects.equals(jwtClaims, that.jwtClaims)
			&& Objects.equals(expiresIn, that.expiresIn)
			&& Objects.equals(refreshToken, that.refreshToken)
			&& Objects.equals(refreshClaims, that.refreshClaims);
	}

	@Override
	public int hashCode() {
		return Objects.hash(jwtToken, jwtClaims, expiresIn, refreshToken, refreshClaims);
	}
}
