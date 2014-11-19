package dev.mobile.traveldiary.adapters;

import java.util.List;

import dev.mobile.traveldiary.models.Place;
import dev.mobile.traveldiary.utils.MyDatabaseHelper;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DiaryListAdapter extends BaseAdapter {

	private Context context;
	
	private List<Place> places;
	
	public DiaryListAdapter(Context context) {
		this.context = context;
		MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
		this.places = dbHelper.getAllPlaces();
	}
	
	@Override
	public int getCount() {
		return (this.places.size());
	}

	@Override
	public Object getItem(int position) {
		return (this.places.get(position));
	}

	@Override
	public long getItemId(int position) {
		return (position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
