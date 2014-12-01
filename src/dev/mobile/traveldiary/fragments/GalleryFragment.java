package dev.mobile.traveldiary.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dev.mobile.traveldiary.R;
import dev.mobile.traveldiary.adapters.ImageGalleryAdapter;
import dev.mobile.traveldiary.models.Picture;
import dev.mobile.traveldiary.models.Place;
import dev.mobile.traveldiary.utils.MyDatabaseHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

public class GalleryFragment extends Fragment {

	private Place place;

	public void setPlace(Place place) {
		this.place = place;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.gallery_fragment_view, container, false);
		GridView gridview = (GridView) rootView.findViewById(R.id.grid_view);
		ImageGalleryAdapter galleryAdapter = new ImageGalleryAdapter(this.getActivity());
		if (place == null) {
			galleryAdapter.setBitmaps(getAllBitmaps());
		} else {
			galleryAdapter.setBitmaps(getPlaceBitmaps(this.place));
			((TextView)(rootView.findViewById(R.id.gallery_title))).setText(place.getName() + "\'s gallery");
		}
		gridview.setAdapter(galleryAdapter);
		return rootView;
	}

	private List<Bitmap> getAllBitmaps() {
		MyDatabaseHelper myDbHelper = new MyDatabaseHelper(this.getActivity());
		List<Picture> pictures = myDbHelper.getAllPictures();
		List<Bitmap> bitmaps = new ArrayList<Bitmap>();
		if (pictures != null) {
			for (Picture p : pictures) {
				File imgFile = new  File(p.getPath());
				if (imgFile.exists()) {
					Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
					bitmaps.add(myBitmap);
				}
			}
		}
		return (bitmaps);
	}

	private List<Bitmap> getPlaceBitmaps(Place place) {
		MyDatabaseHelper myDbHelper = new MyDatabaseHelper(this.getActivity());
		List<Picture> pictures = myDbHelper.getPicturesByPlaceId(place.get_id());
		List<Bitmap> bitmaps = new ArrayList<Bitmap>();
		if (pictures != null) {
			for (Picture p : pictures) {
				File imgFile = new  File(p.getPath());
				if (imgFile.exists()) {
					Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
					bitmaps.add(myBitmap);
				}
			}
		}
		return (bitmaps);
	}

}
