package dev.mobile.traveldiary.fragments;

import java.util.List;

import dev.mobile.traveldiary.R;
import dev.mobile.traveldiary.adapters.DiaryListAdapter;
import dev.mobile.traveldiary.models.Place;
import dev.mobile.traveldiary.utils.MyDatabaseHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DiaryFragment extends Fragment {

	public interface DiaryFragmentListener {
		public void onPlaceEntrySelected(Place place);
	}
	
	private DiaryFragmentListener listener;
	
	private List<Place> places;
	
	public void setListener(DiaryFragmentListener listener) {
		this.listener = listener;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyDatabaseHelper dbHelper = new MyDatabaseHelper(this.getActivity());
		this.places = dbHelper.getAllPlaces();
		View rootView = inflater.inflate(R.layout.diary_fragment_view, container, false);
		ListView listview = (ListView) rootView.findViewById(R.id.diary_list_view);
		final List<Place> places = this.places;
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
				if (listener != null) {
					listener.onPlaceEntrySelected(places.get(position));
				}
			}                 
		});
		DiaryListAdapter adapter = new DiaryListAdapter(this.getActivity(), this.places);
		listview.setAdapter(adapter);
		return rootView;
	}
}
