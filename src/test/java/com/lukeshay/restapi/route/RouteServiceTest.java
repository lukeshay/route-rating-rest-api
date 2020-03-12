package com.lukeshay.restapi.route;

import com.lukeshay.restapi.TestBase;
import com.lukeshay.restapi.gym.Gym;
import com.lukeshay.restapi.wall.Wall;
import com.lukeshay.restapi.wall.WallProperties.WallTypes;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RouteServiceTest extends TestBase {
  private RouteService routeService;
  private Route testRoute;
  private Gym testGym;
  private Wall testWall;

  @BeforeEach
  void setUp() {
    routeService = new RouteServiceImpl(routeRepository, gymRepository, wallRepository);
    testGym = TestBase.createTestGym(user.getId());
    testGym = gymRepository.save(testGym);
    testWall = TestBase.createTestWall(testGym.getId(), Collections.singletonList(WallTypes.LEAD));
    testWall = wallRepository.save(testWall);
    testRoute =
        TestBase.createTestRoute(
            testGym.getId(), testWall.getId(), Collections.singletonList(WallTypes.LEAD));
    testRoute = routeRepository.save(testRoute);

    populateGyms();
    populateWalls();
    populateRoutes();
  }

  /*
  updateRoute
  validateEditor
  validateRoute
  validWallTypes
   */

  @Test
  void createRouteTest() {
    routeRepository.deleteAll();
    testRoute.setId(null);

    Optional<Route> route = routeService.createRoute(testRoute);
    testRoute = routeRepository.findAll().get(0);

    Assertions.assertAll(
        () -> Assertions.assertTrue(route.isPresent()),
        () -> Assertions.assertEquals(testRoute, route.get()));
  }

  @Test
  void deleteRouteTest() {
    Route route = routeService.deleteRoute(authentication, testRoute);
    Assertions.assertEquals(testRoute, route);
  }

  @Test
  void getRoutesByWall() {
    List<Route> routes = routeService.getRoutesByWall(testWall.getId());

    Assertions.assertAll(
        () -> Assertions.assertEquals(1, routes.size()),
        () -> Assertions.assertEquals(testRoute, routes.get(0)));
  }

  @Test
  void getWallTest() {
    Optional<Wall> wall = routeService.getWall(testRoute);

    Assertions.assertAll(
        () -> Assertions.assertTrue(wall.isPresent()),
        () -> Assertions.assertEquals(testWall, wall.get()));

    Route tempRoute = createTestRoute(testGym.getId(), testWall.getId(), testWall.getTypes());
    wallRepository.delete(testWall);

    Optional<Wall> noWall = routeService.getWall(tempRoute);

    Assertions.assertTrue(noWall.isEmpty());
  }
}
