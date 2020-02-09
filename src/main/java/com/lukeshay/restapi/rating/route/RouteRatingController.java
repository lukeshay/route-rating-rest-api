package com.lukeshay.restapi.rating.route;

import com.lukeshay.restapi.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratings/routes")
@PreAuthorize("isAuthenticated()")
@Api(value = "Route rating api endpoints.")
public class RouteRatingController {
  private RouteRatingService ratingService;

  @Autowired
  public RouteRatingController(RouteRatingService ratingService) {
    this.ratingService = ratingService;
  }

  @GetMapping("/{routeId}")
  @PreAuthorize("permitAll()")
  @ApiOperation(value = "Get the ratings of a route.", response = RouteRating.class)
  public ResponseEntity<?> getRatings(
      @PathVariable String routeId,
      @PathParam("sort") String sort,
      @PathParam("limit") Integer limit,
      @PathParam("page") Integer pageNumber) {

    Page<RouteRating> page = ratingService.getRatingsByRouteId(routeId, sort, limit, pageNumber);

    return ResponseUtils.ok(page);
  }

  @PostMapping("")
  @PreAuthorize("isAuthenticated()")
  @ApiOperation(value = "Create a rating for a route.", response = RouteRating.class)
  public ResponseEntity<?> createRating(
      Authentication authentication, @RequestBody RouteRating rating) {
    return ratingService.createRating(authentication, rating);
  }
}
