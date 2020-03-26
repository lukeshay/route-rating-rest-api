package com.lukeshay.restapi.wall;

import com.google.gson.annotations.Expose;
import com.lukeshay.restapi.utils.Auditable;
import com.lukeshay.restapi.utils.ModelUtils;
import com.lukeshay.restapi.wall.WallProperties.WallTypes;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "walls")
public class Wall extends Auditable<String> implements Serializable {

	@Column(name = "id", unique = true, updatable = false)
	@Expose
	@GeneratedValue(generator = "pg-uuid")
	@GenericGenerator(name = "pg-uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Id
	private String id;

	@Column(name = "gym_id")
	@Expose
	private String gymId;

	@Column(name = "name")
	@Expose
	private String name;

	@Column(name = "types")
	@ElementCollection(fetch = FetchType.EAGER)
	@Expose
	private List<WallTypes> types;

	public Wall() {
	}

	public Wall(String gymId, String name, List<WallTypes> types) {
		this.gymId = gymId;
		this.name = name;
		this.types = types;
	}

	public Wall(String id, String gymId, String name, List<WallTypes> types) {
		this.id = id;
		this.gymId = gymId;
		this.name = name;
		this.types = types;
	}

	public String getGymId() {
		return gymId;
	}

	public void setGymId(String gymId) {
		this.gymId = gymId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<WallTypes> getTypes() {
		return types;
	}

	public void setTypes(List<WallTypes> types) {
		this.types = types;
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
		Wall wall = (Wall) o;
		return Objects.equals(id, wall.id)
			&& Objects.equals(gymId, wall.gymId)
			&& Objects.equals(name, wall.name)
			&& ModelUtils.collectionsEqual(types, wall.types);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, gymId, name, types);
	}
}
