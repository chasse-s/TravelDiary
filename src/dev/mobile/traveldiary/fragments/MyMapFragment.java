package dev.mobile.traveldiary.fragments;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class MyMapFragment extends SupportMapFragment  {

	private GoogleMap mapView;

	public MyMapFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater mInflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		return super.onCreateView(mInflater, viewGroup, savedInstanceState);
	}

	@Override
	public void onInflate(Activity activity, AttributeSet attr, Bundle savedInstanceState) {
		super.onInflate(activity, attr, savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mapView = getMap();
	}
}