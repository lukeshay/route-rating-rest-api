package com.lukeshay.restapi;

import com.lukeshay.restapi.aws.AwsService;
import com.lukeshay.restapi.gym.Gym;
import com.lukeshay.restapi.gym.GymRepository;
import com.lukeshay.restapi.rating.route.RouteRatingRepository;
import com.lukeshay.restapi.route.Route;
import com.lukeshay.restapi.route.RouteRepository;
import com.lukeshay.restapi.security.UserPrincipal;
import com.lukeshay.restapi.session.SessionRepository;
import com.lukeshay.restapi.user.User;
import com.lukeshay.restapi.user.UserRepository;
import com.lukeshay.restapi.user.UserTypes;
import com.lukeshay.restapi.wall.Wall;
import com.lukeshay.restapi.wall.WallProperties.WallTypes;
import com.lukeshay.restapi.wall.WallRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TestBase {
	@Autowired
	protected GymRepository gymRepository;
	@Autowired
	protected RouteRatingRepository routeRatingRepository;
	@Autowired
	protected RouteRepository routeRepository;
	@Autowired
	protected SessionRepository sessionRepository;
	@Autowired
	protected UserRepository userRepository;
	@Autowired
	protected WallRepository wallRepository;
	@Autowired
	protected PasswordEncoder passwordEncoder;

	protected User user;
	protected UserPrincipal userPrincipal;

	@Mock
	protected Authentication authentication;
	@Mock
	protected AwsService awsService;

	/**
	 * Helper method to create a test route. The fields not specified are as follows <br>
	 * name: route name <br>
	 * setter: route setter <br>
	 * holdColor: route hold color
	 *
	 * @param gymId  The gym id
	 * @param wallId The wall id
	 * @param types  The type of the wall
	 * @return The route
	 */
	protected static Route createTestRoute(String gymId, String wallId, List<WallTypes> types) {
		return new Route(wallId, gymId, "route name", "route setter", "route hold color", types);
	}

	/**
	 * Helper method to create a test gym. The fields not specified are as follows <br>
	 * name: gym name <br>
	 * address: gym address <br>
	 * city: gym city <br>
	 * state: IA <br>
	 * zipCode: 00000 <br>
	 * website: gym.website.com <br>
	 * email: gym@email.com <br>
	 * phoneNumber: 1111111111
	 *
	 * @param editorId The id of an editor
	 * @return The gym
	 */
	protected static Gym createTestGym(String editorId) {
		return new Gym(
			"gym name",
			"gym address",
			"gym city",
			"IA",
			"00000",
			"gym.website.com",
			"gym@email.com",
			"1111111111",
			Collections.singletonList(editorId));
	}

	/**
	 * Helper method to create a test wall. The fields not specified are as follows <br>
	 * name: wall name <br>
	 *
	 * @param gymId The id of the gym
	 * @param types The types of the wall
	 * @return The wall
	 */
	protected static Wall createTestWall(String gymId, List<WallTypes> types) {
		return new Wall(gymId, "wall name", types);
	}

	@BeforeEach
	protected void setUpClasses() {
		MockitoAnnotations.initMocks(this);

		user =
			new User(
				"test.user@email.com",
				"Test",
				"User",
				"test.user@email.com",
				"1111111111",
				"Des Moines",
				"Iowa",
				"USA",
				"password");

		user.setAuthority(UserTypes.BASIC.authority());
		user.setRole(UserTypes.BASIC.role());

		user = userRepository.save(user);

		userPrincipal = new UserPrincipal(user);

		Mockito.when(authentication.getPrincipal()).thenReturn(userPrincipal);
		Mockito.when(awsService.uploadFileToS3(Mockito.anyString(), Mockito.any(MultipartFile.class)))
			.thenReturn("url.com");
	}

	@AfterEach
	protected void tearDownClasses() {
		gymRepository.deleteAll();
		routeRatingRepository.deleteAll();
		routeRepository.deleteAll();
		sessionRepository.deleteAll();
		userRepository.deleteAll();
		wallRepository.deleteAll();
	}

	protected void populateGyms() {
		for (int i = 0; i < 10; i++) {
			gymRepository.save(
				new Gym(
					"gym name" + i,
					"gym address" + i,
					"gym city" + i,
					"IA",
					"00000",
					"gym.website.com",
					"gym@email.com",
					"1111111111",
					Collections.emptyList()));
		}
	}

	protected void populateWalls() {
		for (int i = 0; i < 10; i++) {
			Gym gym =
				gymRepository.findAll().get((int) Math.ceil(Math.random() * (gymRepository.count() - 1)));
			wallRepository.save(
				new Wall(gym.getId(), "wall name" + i, Collections.singletonList(WallTypes.LEAD)));
		}
	}

	protected void populateRoutes() {
		for (int i = 0; i < 10; i++) {
			Wall wall =
				wallRepository
					.findAll()
					.get((int) Math.ceil(Math.random() * (wallRepository.count() - 1)));
			routeRepository.save(
				new Route(
					wall.getId(),
					wall.getGymId(),
					"route name" + i,
					"route setter" + i,
					"route hold color" + i,
					wall.getTypes()));
		}
	}
}
