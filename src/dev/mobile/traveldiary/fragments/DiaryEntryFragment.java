package dev.mobile.traveldiary.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dev.mobile.traveldiary.R;
import dev.mobile.traveldiary.models.Picture;
import dev.mobile.traveldiary.models.Place;
import dev.mobile.traveldiary.utils.MyDatabaseHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DiaryEntryFragment extends Fragment {

	public interface DiaryEntryFragmentListener {
		public void onEntryEditButtonClicked(Place place);
		public void onGalleryButtonClicked(Place place);
	}

	private DiaryEntryFragmentListener listener;

	private ImageView imageView1;
	private ImageView imageView2;
	private ImageView imageView3;
	
	private ImageButton openGalleryBtn;

	private Place place;

	public void setPlace(Place place) {
		this.place = place;
	}

	public void setListener(DiaryEntryFragmentListener listener) {
		this.listener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.diary_entry_fragment_view, container, false);
		((TextView)rootView.findViewById(R.id.place_name)).setText(this.place.getName());
		((TextView)rootView.findViewById(R.id.place_desc)).setText(this.place.getDescription());
		((TextView)rootView.findViewById(R.id.place_date)).setText(this.place.getDate());
		this.imageView1 = (ImageView)rootView.findViewById(R.id.gallery_image_1);
		this.imageView2 = (ImageView)rootView.findViewById(R.id.gallery_image_2);
		this.imageView3 = (ImageView)rootView.findViewById(R.id.gallery_image_3);
		loadPicture();
		this.openGalleryBtn = (ImageButton)rootView.findViewById(R.id.open_gallery_button);
		this.openGalleryBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onGalleryButtonClicked(place);
				}
			}
		});
		((Button)rootView.findViewById(R.id.edit_button)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onEntryEditButtonClicked(place);
				}
			}
		});

		FragmentManager fmanager = getChildFragmentManager();
		Fragment fragment = fmanager.findFragmentById(R.id.map);
		SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
		GoogleMap supportMap = supportmapfragment.getMap();
		LatLng loc = new LatLng(this.place.getLatitude(), this.place.getLongitude());
		supportMap.addMarker(new MarkerOptions()
		.position(loc)
		.title(place.getName()));
		supportMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
		return (rootView);
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
			}
			if (bitmaps.size() >= 2) {
				imageView2.setImageBitmap(bitmaps.get(1));
			}
			if (bitmaps.size() >= 3) {
				imageView3.setImageBitmap(bitmaps.get(2));
			}
		}
	}
}
