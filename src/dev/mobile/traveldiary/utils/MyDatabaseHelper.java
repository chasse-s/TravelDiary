package dev.mobile.traveldiary.utils;

import java.util.LinkedList;
import java.util.List;

import dev.mobile.traveldiary.models.Place;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper  extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "travelDiaryDb";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_PLACE = "places";

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_DESC = "description";
	private static final String KEY_LONG = "longitude";
	private static final String KEY_LAT = "latitude";
	
	private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_DESC, KEY_LONG, KEY_LAT};

	public MyDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);  
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PLACE_TABLE = "CREATE TABLE " + TABLE_PLACE + " ( " +
				KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				KEY_NAME + " TEXT, " +
				KEY_DESC + " TEXT, " +
				KEY_LONG + " DOUBLE, " + 
				KEY_LAT + " DOUBLE )";
		db.execSQL(CREATE_PLACE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE);
		this.onCreate(db);
	}

	public void addPlace(Place place) {
		Log.d("addPlace", place.toString()); 
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, place.getName());
		values.put(KEY_DESC, place.getDescription());
		values.put(KEY_LONG, place.getLongitude());
		values.put(KEY_LAT, place.getLatitude());
		db.insert(TABLE_PLACE,
				null,
				values);

		db.close(); 
	}

	public Place getPlace(int id){
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = 
				db.query(TABLE_PLACE,
						COLUMNS,
						" id = ?",
						new String[] { String.valueOf(id) },
						null,
						null,
						null,
						null);

		if (cursor != null)
			cursor.moveToFirst();
		Place place = new Place();
		place.set_id(Integer.parseInt(cursor.getString(0)));
		place.setName(cursor.getString(1));
		place.setDescription(cursor.getString(2));
		place.setLongitude(cursor.getDouble(3));
		place.setLatitude(cursor.getDouble(4));
		Log.d("getPlace("+id+")", place.toString());
		return place;
	}

	public List<Place> getAllPlaces() {
		List<Place> places = new LinkedList<Place>();
		String query = "SELECT  * FROM " + TABLE_PLACE;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		Place place = null;
		if (cursor.moveToFirst()) {
			do {
				place = new Place();
				place.set_id(Integer.parseInt(cursor.getString(0)));
				place.setName(cursor.getString(1));
				place.setDescription(cursor.getString(2));
				place.setLongitude(cursor.getDouble(3));
				place.setLatitude(cursor.getDouble(4));
				places.add(place);
			} while (cursor.moveToNext());
		}
		Log.d("getAllPlaces()", places.toString());
		return places;
	}

	public int updatePlace(Place place) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, place.getName());
		values.put(KEY_DESC, place.getDescription());
		values.put(KEY_LONG, place.getLongitude());
		values.put(KEY_LAT, place.getLatitude());
		int i = db.update(TABLE_PLACE,
				values,
				KEY_ID+" = ?",
				new String[] { String.valueOf(place.get_id()) });
		db.close();
		return (i);
	}

	public void deletePlace(Place place) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PLACE,
				KEY_ID+" = ?",
				new String[] { String.valueOf(place.get_id()) });
		db.close();
		Log.d("deletePlace", place.toString());
	}

	public void clearDatabase() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE);
	}

}