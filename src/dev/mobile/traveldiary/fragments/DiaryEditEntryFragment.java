package dev.mobile.traveldiary.fragments;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import dev.mobile.traveldiary.R;
import dev.mobile.traveldiary.models.Picture;
import dev.mobile.traveldiary.models.Place;
import dev.mobile.traveldiary.utils.MyDatabaseHelper;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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
		public void onEntryDeleted();
	}

	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int REQUEST_IMAGE_IMPORT = 2;

	private DiaryEditEntryFragmentListener listener;

	private Place place;

	private double latitude;
	private double longitude;

	private EditText placeNameEdit;
	private EditText placeDescEdit;
	private Button saveButton;
	private Button deleteButton;
	private ImageButton takePhotoButton;
	private ImageButton importPhotoButton;
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
		getActivity();
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			addNewPicture(imageBitmap);
		} else {
			getActivity();
			if (requestCode == REQUEST_IMAGE_IMPORT && resultCode == Activity.RESULT_OK) {
				InputStream stream;
				try {
					stream = this.getActivity().getContentResolver().openInputStream(data.getData());
					BitmapFactory.Options options=new BitmapFactory.Options();
					options.inSampleSize = 8;
					Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
					stream.close();
					addNewPicture(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void addNewPicture(Bitmap imageBitmap) {
		String filename = this.placeNameEdit.getText().toString() + getCurrentDateTime();
		ContextWrapper cw = new ContextWrapper(this.getActivity().getApplicationContext());
		File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
		// Create imageDir
		File mypath = new File(directory, filename);
		FileOutputStream fos = null;
		try {           
			fos = new FileOutputStream(mypath);
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
		if (this.place != null) {
			this.placeNameEdit.setText(place.getName());
			this.placeDescEdit.setText(place.getDescription());
			loadPicture();
		}
		this.takePhotoButton = (ImageButton) rootView.findViewById(R.id.take_photo_button);
		this.takePhotoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startTakeImageIntent();
			}
		});
		this.importPhotoButton = (ImageButton) rootView.findViewById(R.id.import_photo_button);
		this.importPhotoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startImportImageIntent();
			}
		});
		
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
		this.deleteButton = (Button) rootView.findViewById(R.id.delete_button);
		if (this.place != null) {
			this.deleteButton.setVisibility(View.VISIBLE);
			this.deleteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					deletePlace(place);
				}
			});
		}
		return (rootView);
	}

	private void startImportImageIntent() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, REQUEST_IMAGE_IMPORT);
	}
	
	private void startTakeImageIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}
	
	private void loadPicture() {
		MyDatabaseHelper myDbHelper = new MyDatabaseHelper(this.getActivity());
		List<Picture> pictures = myDbHelper.getPicturesByPlaceId(this.place.get_id());
		List<Bitmap> bitmaps = new ArrayList<Bitmap>();
		if (pictures != null) {
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
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		place.setDate(sdf.format(date));
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
		MyDatabaseHelper myDbHelper = new MyDatabaseHelper(this.getActivity());
		for (Picture p : pictures) {
			p.setPlaceId(placeId);
			myDbHelper.addPicture(p);
		}
	}

	private void deletePlace(Place place) {
		MyDatabaseHelper myDbHelper = new MyDatabaseHelper(this.getActivity());
		List<Picture> pictures = myDbHelper.getPicturesByPlaceId(place.get_id());
		for (Picture p : pictures) {
			myDbHelper.deletePicture(p);
		}
		myDbHelper.deletePlace(place);
		if (this.listener != null) {
			this.listener.onEntryDeleted();
		}
	}
}
