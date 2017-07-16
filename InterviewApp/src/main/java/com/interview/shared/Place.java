package com.interview.shared;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
@Table(name = "place")
public class Place {

	@Id
	@Column(name = "PLACE_ID")
	private String placeId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "LOCATION")
	private String location;

	@OneToOne
	@JoinColumn(name = "PLACE_DETAILS_ID", nullable = true)
	private PlaceDetails placeDetails;

	@Column(name = "DIRTY_FLAG")
	private boolean dirtyFlag;

	@ManyToOne
	@JoinColumn(name = "CITY_NAME")
	private City city;

	public Place() {
	}

	@JsonCreator
	public Place(@JsonProperty("placeId") String placeId, @JsonProperty("name") String name,
			@JsonProperty("location") String location) {
		this.placeId = placeId;
		this.name = name;
		this.location = location;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public PlaceDetails getPlaceDetails() {
		return placeDetails;
	}

	public void setPlaceDetails(PlaceDetails placeDetails) {
		this.placeDetails = placeDetails;
	}

	public boolean isDirtyFlag() {
		return dirtyFlag;
	}

	public void setDirtyFlag(boolean dirtyFlag) {
		this.dirtyFlag = dirtyFlag;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
}
