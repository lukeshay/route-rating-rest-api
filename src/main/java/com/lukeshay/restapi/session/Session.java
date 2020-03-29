package com.lukeshay.restapi.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.google.gson.annotations.Expose;
import com.lukeshay.restapi.jwt.RouteRatingJwt;
import com.lukeshay.restapi.utils.Auditable;
import com.lukeshay.restapi.utils.ModelUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "sessions")
public class Session extends Auditable<String> implements Serializable {

	@Column(name = "id", unique = true, updatable = false) @Expose @GeneratedValue(generator = "pg-uuid")
	@GenericGenerator(name = "pg-uuid", strategy = "org.hibernate.id.UUIDGenerator") @Id private String id;

	@Column(name = "tokens") @Expose @Transient private RouteRatingJwt tokens;

	@Column(name = "user_id", unique = true, updatable = false) @Expose private String userId;

	@Column(name = "active") @JsonProperty(access = Access.WRITE_ONLY) private Boolean active;

	public Session() {
	}

	public Session(RouteRatingJwt tokens, String userId) {
		this.tokens = tokens;
		this.userId = userId;
		active = true;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RouteRatingJwt getTokens() {
		return tokens;
	}

	public void setTokens(RouteRatingJwt tokens) {
		this.tokens = tokens;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Session session = (Session) o;
		return Objects.equals(id, session.id) && Objects.equals(tokens, session.tokens) && Objects.equals(
				userId,
				session.userId
		) && Objects.equals(active, session.active);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tokens, userId, active);
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
