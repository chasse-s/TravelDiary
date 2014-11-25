package dev.mobile.traveldiary.adapters;

import java.util.List;

import dev.mobile.traveldiary.R;
import dev.mobile.traveldiary.models.Place;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DiaryListAdapter extends BaseAdapter {

	private Context context;
	
	private List<Place> places;
	
	public DiaryListAdapter(Context context, List<Place> places) {
		this.context = context;
		this.places = places;
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
		Place place = this.places.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService
					(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.diary_list_item, null);
		}
		((TextView)convertView.findViewById(R.id.item_place_name)).setText(place.getName());
		((TextView)convertView.findViewById(R.id.item_place_desc)).setText(place.getDescription());
		return (convertView);
	}

}
