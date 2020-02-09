package com.lukeshay.restapi.rating.route;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRatingRepository extends JpaRepository<RouteRating, String> {

  List<RouteRating> findAllByRouteId(String routeId);

  Page<RouteRating> findAllByRouteId(Pageable pageable, String routeId);
}
