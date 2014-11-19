package dev.mobile.traveldiary.fragments;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import dev.mobile.traveldiary.R;
import dev.mobile.traveldiary.models.Place;
import dev.mobile.traveldiary.utils.MyDatabaseHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class DiaryEditEntryFragment extends Fragment {

	private double latitude;
	private double longitude;

	private EditText placeNameEdit;
	private EditText placeDescEdit;
	private Button saveButton;

	private static final String ARG_LOC_LAT = "location_latitude";
	private static final String ARG_LOC_LONG = "location_longitude";

	public static DiaryEditEntryFragment newInstance(LatLng location) {
		DiaryEditEntryFragment fragment = new DiaryEditEntryFragment();
		Bundle args = new Bundle();
		args.putDouble(ARG_LOC_LAT, location.latitude);
		args.putDouble(ARG_LOC_LONG, location.longitude);
		fragment.setArguments(args);
		return fragment;
	}

	public DiaryEditEntryFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.diary_edit_entry_fragment_view, container, false);
		Bundle args = getArguments();
		this.latitude = args.getDouble(ARG_LOC_LAT);
		this.longitude = args.getDouble(ARG_LOC_LONG);
		this.placeNameEdit = (EditText)rootView.findViewById(R.id.place_name_edit);
		this.placeDescEdit = (EditText)rootView.findViewById(R.id.place_desc_edit);
		this.saveButton = (Button) rootView.findViewById(R.id.save_button);
		this.saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.w("", "on Click");
				addDiaryEntry();
			}
		});
		return (rootView);
	}

	public void addDiaryEntry() {
		Place place = new Place();
		place.setName(this.placeNameEdit.getText().toString());
		place.setDescription(this.placeDescEdit.getText().toString());
		place.setLatitude(this.latitude);
		place.setLongitude(this.longitude);
		Log.w("", "Create new entry, place = " + place.toString());
		MyDatabaseHelper dbHelper = new MyDatabaseHelper(this.getActivity());
		dbHelper.addPlace(place);
		List<Place> places = dbHelper.getAllPlaces();
		Log.w("", "places = " + places.size());
		closeFragment();
	}
	
	private void closeFragment() {
		getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
	}

}
