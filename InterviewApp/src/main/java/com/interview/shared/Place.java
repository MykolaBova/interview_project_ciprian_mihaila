package com.interview.shared;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
@Table(name="PLACES")
public class Place {
	
	public Place() {
	}
	
	@JsonCreator
	public Place(@JsonProperty("placeId") String placeId, @JsonProperty("name") String name, 
			@JsonProperty("location") String location) {
		 this.placeId = placeId;
		 this.name = name;
		 this.location = location;
	}

	@Id
	private String placeId;
	private String name;
	private String location;
	
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
}
