package dev.mobile.traveldiary.activities;


import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import dev.mobile.traveldiary.R;
import dev.mobile.traveldiary.fragments.DiaryEditEntryFragment;
import dev.mobile.traveldiary.fragments.DiaryEntryFragment;
import dev.mobile.traveldiary.fragments.DiaryEntryFragment.DiaryEntryFragmentListener;
import dev.mobile.traveldiary.fragments.DiaryFragment;
import dev.mobile.traveldiary.fragments.DiaryEditEntryFragment.DiaryEditEntryFragmentListener;
import dev.mobile.traveldiary.fragments.DiaryFragment.DiaryFragmentListener;
import dev.mobile.traveldiary.fragments.GalleryFragment;
import dev.mobile.traveldiary.fragments.MyMapFragment;
import dev.mobile.traveldiary.fragments.MyMapFragment.MyMapFragmentListener;
import dev.mobile.traveldiary.fragments.NavigationDrawerFragment;
import dev.mobile.traveldiary.models.Place;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;


public class MainActivity extends ActionBarActivity
implements NavigationDrawerFragment.NavigationDrawerCallbacks, MyMapFragmentListener, DiaryFragmentListener,
DiaryEditEntryFragmentListener, DiaryEntryFragmentListener {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	private static final String FRAGMENT_MAP_TAG = "fragment_map_tag";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		switch (position) {
		case 0:
			displayMap();
			break;
		case 1:
			displayDiary();
			break;
		case 2:
			displayGallery(null);
			break;
		}
	}

	private void displayMap() {
		if (getMapFragment() == null) {
			MyMapFragment mapFragment = new MyMapFragment();
			mapFragment.setListener(this);
			FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
			transaction.add(R.id.container, mapFragment, FRAGMENT_MAP_TAG);
			transaction.addToBackStack(null);
			transaction.commit();
		} else {
			FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
			List<Fragment> fragments = fragmentManager.getFragments();
			int i = fragments.size() - 1;
			while (i >= 0 && !(fragments.get(i) instanceof MyMapFragment)) {
				if (fragments.get(i) != null) {
					fragmentManager.popBackStack();
				}
				i--;
			}
		}
	}

	private void displayDiary() {
		DiaryFragment diaryFragment = new DiaryFragment();
		diaryFragment.setListener(this);
		FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.container, diaryFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	private void displayGallery(Place place) {
		GalleryFragment galleryFragment = new GalleryFragment();
		if (place != null) {
			galleryFragment.setPlace(place);
		}
		FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.container, galleryFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	private void displayDiaryEntry(Place place) {
		DiaryEntryFragment entryFragment = new DiaryEntryFragment();
		entryFragment.setPlace(place);
		entryFragment.setListener(this);
		FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.container, entryFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	private void displayDiaryEditEntry(LatLng location, Place place) {
		if (place != null) {
			location = new LatLng(place.getLatitude(), place.getLongitude());
		}
		FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
		DiaryEditEntryFragment editEntryFragment = DiaryEditEntryFragment.newInstance(location);
		editEntryFragment.setListener(this);
		if (place != null) {
			editEntryFragment.setPlace(place);
			transaction.setCustomAnimations(R.anim.slide_left,
					R.anim.slide_right,
					R.anim.slide_left,
					R.anim.slide_right);
		}
		transaction.add(R.id.container, editEntryFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onNewPlace(LatLng location) {
		displayDiaryEditEntry(location, null);
	}

	@Override
	public void onPlaceEntrySelected(Place place) {
		displayDiaryEntry(place);
	}

	@Override
	public void onPlaceSelected(Place place) {
		displayDiaryEntry(place);
	}

	@Override
	public void onEntryCreated() {
		getSupportFragmentManager().popBackStack();
		Fragment myMapFragment = getMapFragment();
		if (myMapFragment != null) {
			((MyMapFragment) myMapFragment).setUserMarkers();
		}
	}

	@Override
	public void onEntryUpdated(Place place) {
		Fragment myMapFragment = getMapFragment();
		if (myMapFragment != null) {
			((MyMapFragment) myMapFragment).setUserMarkers();
		}
		getSupportFragmentManager().popBackStack();
		getSupportFragmentManager().popBackStack();
		displayDiaryEntry(place);
	}

	private Fragment getMapFragment() {
		return (getSupportFragmentManager().findFragmentByTag(FRAGMENT_MAP_TAG));
	}

	@Override
	public void onEntryEditButtonClicked(Place place) {
		LatLng loc = new LatLng(place.getLatitude(), place.getLongitude());
		displayDiaryEditEntry(loc, place);
	}

	@Override
	public void onGalleryButtonClicked(Place place) {
		displayGallery(place);
	}

	@Override
	public void onEntryDeleted() {
		Fragment myMapFragment = getMapFragment();
		if (myMapFragment != null) {
			((MyMapFragment) myMapFragment).setUserMarkers();
		}
		getSupportFragmentManager().popBackStack();
		getSupportFragmentManager().popBackStack();
	}

}
