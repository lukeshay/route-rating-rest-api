package com.lukeshay.restapi.route;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, String> {

  void deleteByWallId(String wallId);

  List<Route> findAllByWallId(String wallId);
}
