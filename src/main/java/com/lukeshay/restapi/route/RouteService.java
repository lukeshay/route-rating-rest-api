package com.lukeshay.restapi.route;

import com.lukeshay.restapi.wall.Wall;
import com.lukeshay.restapi.wall.WallProperties.WallTypes;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.HttpClientErrorException;

public interface RouteService {
  Logger LOG = LoggerFactory.getLogger(RouteService.class.getName());

  Optional<Route> createRoute(Authentication authentication, Route body);

  List<Route> getRoutesByWall(String wallId);

  Route updateRoute(
      Authentication authentication,
      String id,
      String gymId,
      String wallId,
      List<WallTypes> types,
      String holdColor,
      String setter,
      String name);

  Route deleteRoute(Authentication authentication, Route body);

  Map<String, String> validWallTypes(Route route) throws HttpClientErrorException;

  Optional<Wall> getWall(Route route);

  boolean validateEditor(Authentication authentication, Route body) throws HttpClientErrorException;

  Map<String, String> validateRoute(Route body);
}
