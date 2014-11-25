package dev.mobile.traveldiary.fragments;


import com.google.android.gms.maps.model.LatLng;

import dev.mobile.traveldiary.R;
import dev.mobile.traveldiary.models.Place;
import dev.mobile.traveldiary.utils.MyDatabaseHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class DiaryEditEntryFragment extends Fragment {

	public interface DiaryEditEntryFragmentListener {
		public void onEntryCreated();
		public void onEntryUpdated(Place place);
	}

	private DiaryEditEntryFragmentListener listener;

	private Place place;

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

	public void setPlace(Place place) {
		this.place = place;
	}

	public void setListener(DiaryEditEntryFragmentListener listener) {
		this.listener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.diary_edit_entry_fragment_view, container, false);
		Bundle args = getArguments();
		this.latitude = args.getDouble(ARG_LOC_LAT);
		this.longitude = args.getDouble(ARG_LOC_LONG);
		this.placeNameEdit = (EditText)rootView.findViewById(R.id.place_name_edit);
		this.placeDescEdit = (EditText)rootView.findViewById(R.id.place_desc_edit);
		if (this.place != null) {
			this.placeNameEdit.setText(place.getName());
			this.placeDescEdit.setText(place.getDescription());
		}
		this.saveButton = (Button) rootView.findViewById(R.id.save_button);
		this.saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (place != null) {
					editDiaryEntry();
				} else {
					addDiaryEntry();
				}
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
		MyDatabaseHelper dbHelper = new MyDatabaseHelper(this.getActivity());
		dbHelper.addPlace(place);
		if (this.listener != null) {
			this.listener.onEntryCreated();
		}
	}

	public void editDiaryEntry() {
		this.place.setName(this.placeNameEdit.getText().toString());
		this.place.setDescription(this.placeDescEdit.getText().toString());
		MyDatabaseHelper dbHelper = new MyDatabaseHelper(this.getActivity());
		dbHelper.updatePlace(place);
		if (this.listener != null) {
			this.listener.onEntryUpdated(place);
		}
	}


}
