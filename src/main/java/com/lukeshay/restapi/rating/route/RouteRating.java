package com.lukeshay.restapi.rating.route;

import com.google.gson.annotations.Expose;
import com.lukeshay.restapi.route.RouteProperties.Grade;
import com.lukeshay.restapi.utils.Auditable;
import com.lukeshay.restapi.utils.ModelUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "route_rating")
public class RouteRating extends Auditable<String> implements Serializable {

	@Column(name = "id", unique = true, updatable = false)
	@Expose
	@GeneratedValue(generator = "pg-uuid")
	@GenericGenerator(name = "pg-uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Id
	private String id;

	@Column(name = "creator_id")
	@Expose
	private String creatorId;

	@Column(name = "creator_username")
	@Expose
	private String creatorUsername;

	@Column(name = "route_id")
	@Expose
	private String routeId;

	@Column(name = "review")
	@Expose
	private String review;

	@Column(name = "grade")
	@Expose
	private Grade grade;

	@Column(name = "rating")
	@Expose
	private int rating;

	RouteRating() {
	}

	public RouteRating(String routeId, String review, Grade grade, int rating) {
		this.routeId = routeId;
		this.review = review;
		this.grade = grade;
		this.rating = rating;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorUsername() {
		return creatorUsername;
	}

	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
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
		RouteRating that = (RouteRating) o;
		return rating == that.rating
			&& Objects.equals(id, that.id)
			&& Objects.equals(creatorId, that.creatorId)
			&& Objects.equals(creatorUsername, that.creatorUsername)
			&& Objects.equals(routeId, that.routeId)
			&& Objects.equals(review, that.review)
			&& grade == that.grade;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, creatorId, creatorUsername, routeId, review, grade, rating);
	}
}
