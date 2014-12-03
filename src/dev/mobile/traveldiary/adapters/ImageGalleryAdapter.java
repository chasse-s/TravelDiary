package dev.mobile.traveldiary.adapters;

import java.util.List;

import dev.mobile.traveldiary.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageGalleryAdapter extends BaseAdapter {

	private Context context;

	private List<Bitmap> bitmaps;

	public ImageGalleryAdapter(Context context) {
		this.context = context;
	}

	public void setBitmaps(List<Bitmap> bitmaps) {
		this.bitmaps = bitmaps;
	}

	@Override
	public int getCount() {
		return (bitmaps != null ? bitmaps.size() : 0);
	}

	@Override
	public Object getItem(int position) {
		return (bitmaps != null ? bitmaps.get(position) : null);
	}

	@Override
	public long getItemId(int position) {
		return (bitmaps != null ? bitmaps.size() : 0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(this.context);
			imageView.setLayoutParams(new GridView.LayoutParams(210, 280));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(5, 5, 5, 5);
			imageView.setBackground(this.context.getResources().getDrawable(R.drawable.cadre));
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setImageBitmap(this.bitmaps.get(position));
		return (imageView);
	}
}