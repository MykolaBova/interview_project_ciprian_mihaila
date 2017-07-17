package com.interview.shared;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "photo_reference")
public class PhotoReference {

	@Id
	@Column(name = "reference")
	private String photoReference;

	@ManyToOne
	@JoinColumn(name = "place_details_id")
	@JsonBackReference
	private PlaceDetails placeDetails;

	public PhotoReference() {
	}

	public PhotoReference(@JsonProperty("photoReference") String photoReference,
			@JsonProperty("placeDetails") PlaceDetails placeDetails) {
		this.setPhotoReference(photoReference);
		this.setPlaceDetails(placeDetails);
	}

	public String getPhotoReference() {
		return photoReference;
	}

	public void setPhotoReference(String photoReference) {
		this.photoReference = photoReference;
	}

	public PlaceDetails getPlaceDetails() {
		return placeDetails;
	}

	public void setPlaceDetails(PlaceDetails placeDetails) {
		this.placeDetails = placeDetails;
	}

}
