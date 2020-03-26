package com.lukeshay.restapi.v2.gym;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.google.gson.annotations.Expose;
import com.lukeshay.restapi.gym.Gym;
import com.lukeshay.restapi.utils.ModelUtils;

import java.util.List;
import java.util.Objects;

public class GymWithWalls extends Gym {

	@Expose
	@JsonProperty(access = Access.READ_WRITE)
	private List<WallWithRoutes> walls;

	public GymWithWalls(Gym gym, List<WallWithRoutes> walls) {
		super(
			gym.getId(),
			gym.getName(),
			gym.getAddress(),
			gym.getCity(),
			gym.getState(),
			gym.getZipCode(),
			gym.getWebsite(),
			gym.getEmail(),
			gym.getPhoneNumber(),
			gym.getLogoUrl(),
			gym.getPhotoUrl(),
			gym.getAuthorizedEditors());
		this.walls = walls;
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
		if (!super.equals(o)) {
			return false;
		}
		GymWithWalls that = (GymWithWalls) o;
		return Objects.equals(walls, that.walls);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), walls);
	}
}
