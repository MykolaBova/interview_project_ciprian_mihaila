package com.interview.shared;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "places_details")
public class PlaceDetails {

	@Id
	@Column(name = "PLACE_DETAILS_ID")
	private String placeId;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "PHONE")
	private String phone;

	@Column(name = "ICON")
	private String icon;

	@OneToMany(mappedBy = "placeDetails", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<PhotoReference> photosReferences;

	public PlaceDetails() {
	}

	public PlaceDetails(@JsonProperty("placeId") String placeId, @JsonProperty("address") String address,
			@JsonProperty("phone") String phone, @JsonProperty("icon") String icon) {
		this.placeId = placeId;
		this.address = address;
		this.phone = phone;
		this.icon = icon;
		this.setPhotosReferences(photosReferences);
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<PhotoReference> getPhotosReferences() {
		return photosReferences;
	}

	public void setPhotosReferences(List<PhotoReference> photosReferences) {
		this.photosReferences = photosReferences;
	}

}
