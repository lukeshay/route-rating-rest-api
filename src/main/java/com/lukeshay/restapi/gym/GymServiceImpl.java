package com.lukeshay.restapi.gym;

import com.lukeshay.restapi.aws.AwsService;
import com.lukeshay.restapi.user.User;
import com.lukeshay.restapi.user.UserTypes;
import com.lukeshay.restapi.utils.AuthenticationUtils;
import com.lukeshay.restapi.utils.BodyUtils;
import com.lukeshay.restapi.utils.PageableUtils;
import com.lukeshay.restapi.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class GymServiceImpl implements GymService {

	private GymRepository gymRepository;
	private AwsService awsService;

	@Autowired
	public GymServiceImpl(GymRepository gymRepository, AwsService awsService) {
		this.gymRepository = gymRepository;
		this.awsService = awsService;
	}

	@Override
	public Gym createGym(Gym gym) {
		return gymRepository.save(gym);
	}

	@Deprecated
	@Override
	public Iterable<Gym> getAllGyms() {
		return gymRepository.findAll();
	}

	@Override
	public Gym getGymById(String gymId) {
		return gymRepository.findById(gymId).orElse(null);
	}

	@Override
	public Page<Gym> getGyms(String query, String sorts, Integer limit, Integer page) {
		if (query == null) {
			query = "";
		}

		return gymRepository.findAllByNameIgnoreCaseContaining(PageableUtils.buildPageRequest(page, limit, sorts),
				query
		);
	}

	@Override
	public Gym updateGym(
			Authentication authentication,
			String gymId,
			String name,
			String address,
			String city,
			String state,
			String zipCode,
			String email,
			String phoneNumber,
			String website,
			List<String> authorizedEditors
	) {

		Gym gym = gymRepository.findById(gymId).orElse(null);
		User user = AuthenticationUtils.getUser(authentication);

		if (gym == null || user == null || ((gym.getAuthorizedEditors() == null || !gym.getAuthorizedEditors()
				.contains(user.getId())) && !user.getAuthority().equals(UserTypes.ADMIN.authority()))) {
			return null;
		}

		gym.setNameIfNotNull(name);
		gym.setName(name);
		gym.setAddressIfNotNull(address);
		gym.setCityIfNotNull(city);
		gym.setStateIfNotNull(state);
		gym.setZipCodeIfNotNull(zipCode);
		gym.setWebsiteIfNotNull(website);
		gym.setPhoneNumberIfNotNull(phoneNumber);
		gym.setEmailIfNotNull(email);
		gym.setAuthorizedEditorsIfNotNull(authorizedEditors);

		return gymRepository.save(gym);
	}

	@Override
	public ResponseEntity<?> uploadPhoto(
			Authentication authentication, MultipartFile file, String gymId, String imageName
	) {
		Gym gym = gymRepository.findById(gymId).orElse(null);
		User user = AuthenticationUtils.getUser(authentication);

		if (gym == null || user == null || ((gym.getAuthorizedEditors() == null || !gym.getAuthorizedEditors()
				.contains(user.getId())) && !user.getAuthority().equals(UserTypes.ADMIN.authority()))) {
			return ResponseUtils.unauthorized(BodyUtils.error("You are unauthorized to perform this action."));
		}

		if (!imageName.equals("logo") && !imageName.equals("gym")) {
			return ResponseUtils.badRequest(BodyUtils.error("Invalid upload."));
		}

		String url = awsService.uploadFileToS3(String.format("gyms/%s/%s.jpg", gym.getId(), imageName), file);

		if (url == null) {
			return ResponseUtils.internalServerError(BodyUtils.error("Error uploading file."));
		}
		else {
			if (imageName.equals("logo")) {
				gym.setLogoUrl(url);
			}
			else {
				gym.setPhotoUrl(url);
			}

			gym = gymRepository.save(gym);
			return ResponseUtils.ok(gym);
		}
	}
}
