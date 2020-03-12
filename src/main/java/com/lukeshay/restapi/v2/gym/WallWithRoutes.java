package com.lukeshay.restapi.v2.gym;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.google.gson.annotations.Expose;
import com.lukeshay.restapi.route.Route;
import com.lukeshay.restapi.utils.ModelUtils;
import com.lukeshay.restapi.wall.Wall;
import java.util.List;
import java.util.Objects;

public class WallWithRoutes extends Wall {

  @Expose
  @JsonProperty(access = Access.READ_WRITE)
  private List<Route> routes;

  public WallWithRoutes(Wall wall, List<Route> routes) {
    super(wall.getId(), wall.getGymId(), wall.getName(), wall.getTypes());
    this.routes = routes;
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
    WallWithRoutes that = (WallWithRoutes) o;
    return Objects.equals(routes, that.routes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), routes);
  }
}
