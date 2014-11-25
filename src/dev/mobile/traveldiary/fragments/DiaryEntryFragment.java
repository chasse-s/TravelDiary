package dev.mobile.traveldiary.fragments;

import dev.mobile.traveldiary.R;
import dev.mobile.traveldiary.models.Place;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DiaryEntryFragment extends Fragment {
	
	public interface DiaryEntryFragmentListener {
		public void onEntryEditButtonClicked(Place place);
	}
	
	private DiaryEntryFragmentListener listener;
	
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
		((Button)rootView.findViewById(R.id.edit_button)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onEntryEditButtonClicked(place);
				}
			}
		});
		return (rootView);
	}
}
