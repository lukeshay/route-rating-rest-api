package com.lukeshay.restapi.gym;

import com.google.gson.annotations.Expose;
import com.lukeshay.restapi.utils.Auditable;
import com.lukeshay.restapi.utils.ModelUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * The type Gym.
 */
@Entity
@Table(name = "gyms")
public class Gym extends Auditable<String> implements Serializable {

	@Column(name = "id", unique = true, updatable = false) @Expose @GeneratedValue(generator = "pg-uuid")
	@GenericGenerator(name = "pg-uuid", strategy = "org.hibernate.id.UUIDGenerator") @Id private String id;

	@Column(name = "name") @Expose private String name;

	@Column(name = "address") @Expose private String address;

	@Column(name = "city") @Expose private String city;

	@Column(name = "state") @Expose private String state;

	@Column(name = "zip_code") @Expose private String zipCode;

	@Column(name = "website") @Expose private String website;

	@Column(name = "email") @Expose private String email;

	@Column(name = "phone_number") @Expose private String phoneNumber;

	@Column(name = "logo_url") @Expose private String logoUrl;

	@Column(name = "photo_url") @Expose private String photoUrl;

	@Column(name = "authorized_editors") @ElementCollection(fetch = FetchType.EAGER) @Expose private List<String>
			authorizedEditors;

	public Gym() {
	}

	public Gym(
			String name,
			String address,
			String city,
			String state,
			String zipCode,
			String website,
			String email,
			String phoneNumber,
			List<String> authorizedEditors
	) {
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.website = website;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.authorizedEditors = authorizedEditors;
	}

	public Gym(
			String id,
			String name,
			String address,
			String city,
			String state,
			String zipCode,
			String website,
			String email,
			String phoneNumber,
			String logoUrl,
			String photoUrl,
			List<String> authorizedEditors
	) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.website = website;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.logoUrl = logoUrl;
		this.photoUrl = photoUrl;
		this.authorizedEditors = authorizedEditors;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<String> getAuthorizedEditors() {
		return authorizedEditors;
	}

	public void setAuthorizedEditors(List<String> authorizedEditors) {
		this.authorizedEditors = authorizedEditors;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setAddressIfNotNull(String address) {
		if (address != null && !address.isBlank()) {
			this.address = address;
		}
	}

	public void setAuthorizedEditorsIfNotNull(List<String> authorizedEditors) {
		if (authorizedEditors != null && authorizedEditors.isEmpty()) {
			this.authorizedEditors = authorizedEditors;
		}
	}

	public void setCityIfNotNull(String city) {
		if (city != null && !city.isBlank()) {
			this.city = city;
		}
	}

	public void setEmailIfNotNull(String email) {
		if (email != null && !email.isBlank()) {
			this.email = email;
		}
	}

	public void setIdIfNotNull(String id) {
		if (id != null && !id.isBlank()) {
			this.id = id;
		}
	}

	public void setLogoUrlIfNotNull(String logoUrl) {
		if (logoUrl != null && !logoUrl.isBlank()) {
			this.logoUrl = logoUrl;
		}
	}

	public void setNameIfNotNull(String name) {
		if (name != null && !name.isBlank()) {
			this.name = name;
		}
	}

	public void setPhoneNumberIfNotNull(String phoneNumber) {
		if (phoneNumber != null && !phoneNumber.isBlank()) {
			this.phoneNumber = phoneNumber;
		}
	}

	public void setPhotoUrlIfNotNull(String photoUrl) {
		if (photoUrl != null && !photoUrl.isBlank()) {
			this.photoUrl = photoUrl;
		}
	}

	public void setStateIfNotNull(String state) {
		if (state != null && !state.isBlank()) {
			this.state = state;
		}
	}

	public void setWebsiteIfNotNull(String website) {
		if (website != null && !website.isBlank()) {
			this.website = website;
		}
	}

	public void setZipCodeIfNotNull(String zipCode) {
		if (zipCode != null && !zipCode.isBlank()) {
			this.zipCode = zipCode;
		}
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
		Gym gym = (Gym) o;
		return Objects.equals(id, gym.id) && Objects.equals(name, gym.name) && Objects.equals(address,
				gym.address
		) && Objects.equals(city, gym.city) && Objects.equals(
				state,
				gym.state
		) && Objects.equals(zipCode, gym.zipCode) && Objects.equals(website, gym.website) && Objects.equals(
				email,
				gym.email
		) && Objects.equals(
				phoneNumber,
				gym.phoneNumber
		) && Objects.equals(logoUrl, gym.logoUrl) && Objects.equals(
				photoUrl,
				gym.photoUrl
		) && ModelUtils.collectionsEqual(authorizedEditors, gym.authorizedEditors);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id,
				name,
				address,
				city,
				state,
				zipCode,
				website,
				email,
				phoneNumber,
				logoUrl,
				photoUrl,
				authorizedEditors
		);
	}
}
