package dev.mobile.traveldiary.models;

public class Picture {

	private int _id;
	private String path;
	private int placeId;
	
	public Picture() {
		super();
	}
	
	public Picture(String path, int placeId) {
		super();
		this.path = path;
		this.placeId = placeId;
	}

	public int get_id() {
		return _id;
	}
	
	public void set_id(int _id) {
		this._id = _id;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public int getPlaceId() {
		return placeId;
	}
	
	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}
	
	@Override
	public String toString() {
		return "Picture [_id=" + _id + ", path=" + path + ", placeId="
				+ placeId + "]";
	}

}
