package dev.mobile.traveldiary.models;

public class Place {

	private int _id;
	private String name;
	private String description;
	private Double longitude;
	private Double latitude;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "Place [_id=" + _id + ", name=" + name + ", description="
				+ description + ", longitude=" + longitude + ", latitude="
				+ latitude + "]";
	}

}
