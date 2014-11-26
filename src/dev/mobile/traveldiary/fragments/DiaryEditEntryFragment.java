package dev.mobile.traveldiary.fragments;


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import dev.mobile.traveldiary.R;
import dev.mobile.traveldiary.models.Picture;
import dev.mobile.traveldiary.models.Place;
import dev.mobile.traveldiary.utils.MyDatabaseHelper;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class DiaryEditEntryFragment extends Fragment {

	public interface DiaryEditEntryFragmentListener {
		public void onEntryCreated();
		public void onEntryUpdated(Place place);
	}

	static final int REQUEST_IMAGE_CAPTURE = 1;

	private DiaryEditEntryFragmentListener listener;

	private Place place;

	private double latitude;
	private double longitude;

	private EditText placeNameEdit;
	private EditText placeDescEdit;
	private Button saveButton;
	private ImageButton takePhotoButton;
	private ImageView imageView1;
	private ImageView imageView2;
	private ImageView imageView3;

	private int imageEmptyPos = 1;
	
	private List<Picture> picturesToAdd;

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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
			Log.w("DiaryEntryEdit.java", "onActivityResult() : result ok");
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			String filename = this.placeNameEdit.getText().toString() + getCurrentDateTime();

			ContextWrapper cw = new ContextWrapper(this.getActivity().getApplicationContext());
			// path to /data/data/yourapp/app_data/imageDir
			File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
			// Create imageDir
			File mypath = new File(directory, filename);
			FileOutputStream fos = null;
			try {           
				fos = new FileOutputStream(mypath);
				// Use the compress method on the BitMap object to write image to the OutputStream
				imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.close();
				Picture newPic = new Picture();
				newPic.setPath(directory.getAbsolutePath() + "/" + filename);
				this.picturesToAdd.add(newPic);
				if (this.imageEmptyPos == 1) {
					imageView1.setImageBitmap(imageBitmap);
					this.imageEmptyPos = 2;
				} else if (this.imageEmptyPos == 2) {
					imageView2.setImageBitmap(imageBitmap);
					this.imageEmptyPos = 3;
				} else if (this.imageEmptyPos == 3) {
					imageView3.setImageBitmap(imageBitmap);
					this.imageEmptyPos = 0;
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String getCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String currentDateandTime = sdf.format(new Date());
		return (currentDateandTime);
	}

	public DiaryEditEntryFragment() {
		this.picturesToAdd = new ArrayList<Picture>();
	}

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
		this.imageView1 = (ImageView)rootView.findViewById(R.id.gallery_image_1);
		this.imageView2 = (ImageView)rootView.findViewById(R.id.gallery_image_2);
		this.imageView3 = (ImageView)rootView.findViewById(R.id.gallery_image_3);
		this.takePhotoButton = (ImageButton) rootView.findViewById(R.id.take_photo_button);
		this.takePhotoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
					startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
				}
			}
		});
		if (this.place != null) {
			this.placeNameEdit.setText(place.getName());
			this.placeDescEdit.setText(place.getDescription());
			loadPicture();
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

	private void loadPicture() {
		MyDatabaseHelper myDbHelper = new MyDatabaseHelper(this.getActivity());
		List<Picture> pictures = myDbHelper.getPicturesByPlaceId(this.place.get_id());
		List<Bitmap> bitmaps = new ArrayList<Bitmap>();
		if (pictures != null) {
			Log.w("DiaryEditEntryFragment.java", "loadPicture(): "+ pictures.size() + " to load.");
			for (Picture p : pictures) {
				File imgFile = new  File(p.getPath());
				if (imgFile.exists()) {
					Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
					bitmaps.add(myBitmap);
				}
			}
			if (bitmaps.size() >= 1) {
				imageView1.setImageBitmap(bitmaps.get(0));
				this.imageEmptyPos = 2;
			}
			if (bitmaps.size() >= 2) {
				imageView2.setImageBitmap(bitmaps.get(1));
				this.imageEmptyPos = 3;
			}
			if (bitmaps.size() >= 3) {
				imageView3.setImageBitmap(bitmaps.get(2));
				this.imageEmptyPos = 0;
			}
		}
	}

	public void addDiaryEntry() {
		Place place = new Place();
		place.setName(this.placeNameEdit.getText().toString());
		place.setDescription(this.placeDescEdit.getText().toString());
		place.setLatitude(this.latitude);
		place.setLongitude(this.longitude);
		MyDatabaseHelper dbHelper = new MyDatabaseHelper(this.getActivity());
		int placeId = dbHelper.addPlace(place);
		if (this.picturesToAdd != null) {
			savePictures(this.picturesToAdd, placeId);
		}
		if (this.listener != null) {
			this.listener.onEntryCreated();
		}
	}

	public void editDiaryEntry() {
		this.place.setName(this.placeNameEdit.getText().toString());
		this.place.setDescription(this.placeDescEdit.getText().toString());
		MyDatabaseHelper dbHelper = new MyDatabaseHelper(this.getActivity());
		dbHelper.updatePlace(place);
		if (this.picturesToAdd != null) {
			savePictures(this.picturesToAdd, this.place.get_id());
		}
		if (this.listener != null) {
			this.listener.onEntryUpdated(place);
		}
	}

	private void savePictures(List<Picture> pictures, int placeId) {
		Log.e("DiaryEditEntryFragment.java", "savePictures()");
		MyDatabaseHelper myDbHelper = new MyDatabaseHelper(this.getActivity());
		for (Picture p : pictures) {
			Log.w("DiaryEditEntryFragment.java", "savePictures() : " + p.getPath());
			p.setPlaceId(placeId);
			myDbHelper.addPicture(p);
		}
	}

}
