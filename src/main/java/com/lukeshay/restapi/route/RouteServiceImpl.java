package com.lukeshay.restapi.route;

import com.lukeshay.restapi.gym.Gym;
import com.lukeshay.restapi.gym.GymRepository;
import com.lukeshay.restapi.user.User;
import com.lukeshay.restapi.utils.AuthenticationUtils;
import com.lukeshay.restapi.utils.ExceptionUtils;
import com.lukeshay.restapi.wall.Wall;
import com.lukeshay.restapi.wall.WallProperties.WallTypes;
import com.lukeshay.restapi.wall.WallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RouteServiceImpl implements RouteService {

	private RouteRepository routeRepository;
	private GymRepository gymRepository;
	private WallRepository wallRepository;

	@Autowired
	public RouteServiceImpl(
			RouteRepository routeRepository, GymRepository gymRepository, WallRepository wallRepository
	) {
		this.routeRepository = routeRepository;
		this.gymRepository = gymRepository;
		this.wallRepository = wallRepository;
	}

	@Override
	public Optional<Route> createRoute(Route body) {
		try {
			Route route = routeRepository.save(body);
			return Optional.of(route);
		} catch (Exception ignored) {
			return Optional.empty();
		}
	}

	@Override
	public Route deleteRoute(Authentication authentication, Route body) {
		User user = AuthenticationUtils.getUser(authentication);
		Gym gym = gymRepository.findById(body.getGymId()).orElse(null);

		if (body.getId() == null) {
			return null;
		}

		Route route = routeRepository.findById(body.getId()).orElse(null);

		if (route == null || gym == null || user == null || !route.getGymId()
		                                                          .equals(body.getGymId()) || !gym.getAuthorizedEditors()
		                                                                                          .contains(user.getId())) {
			return null;
		}

		routeRepository.deleteById(route.getId());

		return route;
	}

	private Gym getGymOrNotFound(Route route) throws HttpClientErrorException {
		Optional<Gym> gym = gymRepository.findById(route.getGymId());

		if (!gym.isPresent()) {
			throw ExceptionUtils.notFound("Gym not found.");
		}

		return gym.get();
	}

	@Override
	public List<Route> getRoutesByWall(String wallId) {
		return routeRepository.findAllByWallId(wallId);
	}

	@Override
	public Optional<Wall> getWall(Route route) {
		if (route.getWallId() != null) {
			return wallRepository.findById(route.getWallId());
		}
		else {
			return Optional.empty();
		}
	}

	private Wall getWallOrNotFound(Route route) throws HttpClientErrorException {
		Optional<Wall> wallOptional = getWall(route);

		if (!wallOptional.isPresent()) {
			throw ExceptionUtils.notFound("Wall not found.");
		}

		return wallOptional.get();
	}

	@Override
	public Route updateRoute(
			Authentication authentication,
			String id,
			String gymId,
			String wallId,
			List<WallTypes> types,
			String holdColor,
			String setter,
			String name
	) {
		User user = AuthenticationUtils.getUser(authentication);
		Route route = routeRepository.findById(id).orElse(null);
		Gym gym = gymRepository.findById(gymId).orElse(null);

		if (route == null || !route.getGymId()
		                           .equals(gymId) || gym == null || user == null || !gym.getAuthorizedEditors()
		                                                                                .contains(user.getId())) {
			return null;
		}

		if (wallId != null && !wallId.equals("")) {
			Wall newWall = wallRepository.findById(wallId).orElse(null);

			if (newWall != null && newWall.getGymId() != null && newWall.getGymId().equals(gymId)) {
				route.setWallId(wallId);
			}
			else {
				return null;
			}
		}

		if (types != null && types.size() > 0) {
			route.setTypes(types);
		}

		if (holdColor != null && !holdColor.equals("")) {
			route.setHoldColor(holdColor);
		}

		if (setter != null && !setter.equals("")) {
			route.setSetter(setter);
		}

		if (name != null && !name.equals("")) {
			route.setName(name);
		}

		return routeRepository.save(route);
	}

	@Override
	public Map<String, String> validWallTypes(Route route) throws HttpClientErrorException {
		Wall wall = getWallOrNotFound(route);
		Map<String, String> result = new HashMap<>();

		route.getTypes().forEach((wallType) -> {
			if (!wall.getTypes().contains(wallType)) {
				result.put("types", wallType.toString() + " is not allowed for this wall.");
			}
		});

		return result;
	}

	@Override
	public boolean validateEditor(Authentication authentication, Route body) throws HttpClientErrorException {
		Optional<User> user = AuthenticationUtils.getUserOptional(authentication);
		Gym gym = getGymOrNotFound(body);

		return user.isPresent() && gym.getAuthorizedEditors().contains(user.get().getId());
	}

	@Override
	public Map<String, String> validateRoute(Route body) {
		getWallOrNotFound(body);
		getGymOrNotFound(body);
		return new HashMap<>();
	}
}
