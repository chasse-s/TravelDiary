package dev.mobile.traveldiary.fragments;

import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dev.mobile.traveldiary.R;
import dev.mobile.traveldiary.models.Place;
import dev.mobile.traveldiary.utils.MyDatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MyMapFragment extends SupportMapFragment implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
LocationListener {

	public interface MyMapFragmentListener {
		public void onNewLocation(LatLng location);
	}

	private MyMapFragmentListener listener;

	private GoogleMap googleMap;

	private LocationRequest locationRequest;

	private LocationClient clientLocation;

	public MyMapFragment() {}

	public void setListener(MyMapFragmentListener listener) {
		this.listener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.locationRequest = LocationRequest.create();
		this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		this.clientLocation = new LocationClient(this.getActivity().getApplicationContext(), this, this);
		this.clientLocation.connect();
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
		this.googleMap = getMap();
		this.googleMap.setMyLocationEnabled(true);
		MapsInitializer.initialize(this.getActivity());

		this.addUserMarkers();
	
		this.googleMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng point) {
				askForNewLocation(point);
			}
		});
	}
	
	private void addUserMarkers() {
		MyDatabaseHelper dbHelper = new MyDatabaseHelper(this.getActivity());
		List<Place> places = dbHelper.getAllPlaces();
		for (Place place : places) {
			this.googleMap.addMarker(new MarkerOptions()
			.position(new LatLng(place.getLatitude(), place.getLongitude()))
			.title(place.getName())).showInfoWindow();
		}
	}

	private void askForNewLocation(final LatLng location) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
		alertDialogBuilder
		.setMessage(this.getResources().getString(R.string.add_loc_dialog))
		.setCancelable(true)
		.setPositiveButton(this.getResources().getString(R.string.yes), 
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				createNewDiaryEntry(location);
			}
		})
		.setNegativeButton(this.getResources().getString(R.string.no), 
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void createNewDiaryEntry(LatLng location) {
		if (this.listener != null) {
			this.listener.onNewLocation(location);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
				new LatLng(location.getLatitude(), location.getLongitude()), 15);
		this.googleMap.animateCamera(cameraUpdate);
	}

	@Override
	public void onConnectionFailed(ConnectionResult conn) {}

	@Override
	public void onConnected(Bundle bundle) {
		this.clientLocation.requestLocationUpdates(this.locationRequest, this);
	}

	@Override
	public void onDisconnected() {
		if (this.clientLocation.isConnected()) {
			this.clientLocation.disconnect();
		}
	}

}