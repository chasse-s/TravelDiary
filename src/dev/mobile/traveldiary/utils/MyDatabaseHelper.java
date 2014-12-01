package dev.mobile.traveldiary.utils;

import java.util.LinkedList;
import java.util.List;

import dev.mobile.traveldiary.models.Picture;
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
	private static final String TABLE_PICTURE = "pictures";


	private static final String KEY_ID = "id";

	private static final String KEY_NAME = "name";
	private static final String KEY_DESC = "description";
	private static final String KEY_LONG = "longitude";
	private static final String KEY_LAT = "latitude";
	private static final String KEY_CREATED_AT = "created_at";

	private static final String KEY_PATH = "path";
	private static final String KEY_PLACE_ID = "picture_id";

	private static final String[] PLACE_COLUMNS = {KEY_ID,KEY_NAME,KEY_DESC, KEY_LONG, KEY_LAT, KEY_CREATED_AT};
	private static final String[] PICTURE_COLUMNS = {KEY_ID,KEY_PATH,KEY_PLACE_ID};
	
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
				KEY_LAT + " DOUBLE, " +
				KEY_CREATED_AT + " ) ";
		db.execSQL(CREATE_PLACE_TABLE);
		String CREATE_PICTURE_TABLE = "CREATE TABLE " + TABLE_PICTURE + " ( " +
				KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				KEY_PATH + " TEXT, " +
				KEY_PLACE_ID + " INTEGER )";
		db.execSQL(CREATE_PICTURE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURE);
		this.onCreate(db);
	}

	public void clearDatabase() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURE);
	}

	public int addPlace(Place place) {
		Log.d("addPlace", place.toString()); 
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, place.getName());
		values.put(KEY_DESC, place.getDescription());
		values.put(KEY_LONG, place.getLongitude());
		values.put(KEY_LAT, place.getLatitude());
		values.put(KEY_CREATED_AT, place.getDate());
		long id = db.insert(TABLE_PLACE,
				null,
				values);

		db.close();
		Log.w("MyDatabaseHelper.java", "place "+ id +"added.");
		return ((int)id);
	}

	public Place getPlace(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = 
				db.query(TABLE_PLACE,
						PLACE_COLUMNS,
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
		place.setDate(cursor.getString(5));
		Log.d("getPlace("+id+")", place.toString());
		return place;
	}

	public Place getPlaceByName(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = 
				db.query(TABLE_PLACE,
						PLACE_COLUMNS,
						" "+ KEY_NAME +" = ?",
						new String[] { name },
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
		place.setDate(cursor.getString(5));
		Log.d("getPlace("+name+")", place.toString());
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
				place.setDate(cursor.getString(5));
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
		values.put(KEY_CREATED_AT, place.getDate());
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


	// PICTURES

	public void addPicture(Picture picture) {
		Log.d("addPicture", picture.toString()); 
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PATH, picture.getPath());
		values.put(KEY_PLACE_ID, picture.getPlaceId());
		db.insert(TABLE_PICTURE,
				null,
				values);
		db.close();
		Log.w("MyDatabaseHelper.java", "picture added.");
	}

	public Picture getPicture(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = 
				db.query(TABLE_PICTURE,
						PICTURE_COLUMNS,
						" id = ?",
						new String[] { String.valueOf(id) },
						null,
						null,
						null,
						null);

		if (cursor != null)
			cursor.moveToFirst();
		Picture picture = new Picture();
		picture.set_id(Integer.parseInt(cursor.getString(0)));
		picture.setPath(cursor.getString(1));
		picture.setPlaceId(cursor.getInt(2));
		Log.d("getPicture("+id+")", picture.toString());
		return picture;
	}

	public List<Picture> getPicturesByPlaceId(Integer placeId) {
		List<Picture> pictures = new LinkedList<Picture>();
		String query = "SELECT  * FROM " + TABLE_PICTURE + " WHERE " + KEY_PLACE_ID + " = " + placeId;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		Picture picture = null;
		if (cursor.moveToFirst()) {
			do {
				picture = new Picture();
				picture.set_id(Integer.parseInt(cursor.getString(0)));
				picture.setPath(cursor.getString(1));
				picture.setPlaceId(cursor.getInt(2));
				pictures.add(picture);
			} while (cursor.moveToNext());
		}
		Log.d("getAllPictures()", pictures.toString());
		return pictures;
	}

	public List<Picture> getAllPictures() {
		List<Picture> pictures = new LinkedList<Picture>();
		String query = "SELECT  * FROM " + TABLE_PICTURE;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		Picture picture = null;
		if (cursor.moveToFirst()) {
			do {
				picture = new Picture();
				picture.set_id(Integer.parseInt(cursor.getString(0)));
				picture.setPath(cursor.getString(1));
				picture.setPlaceId(cursor.getInt(2));
				pictures.add(picture);
			} while (cursor.moveToNext());
		}
		Log.d("getAllPictures()", pictures.toString());
		return pictures;
	}

	public int updatePicture(Picture picture) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PATH, picture.getPath());
		values.put(KEY_PLACE_ID, picture.getPlaceId());
		int i = db.update(TABLE_PICTURE,
				values,
				KEY_ID+" = ?",
				new String[] { String.valueOf(picture.get_id()) });
		db.close();
		return (i);
	}

	public void deletePicture(Picture picture) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PICTURE,
				KEY_ID+" = ?",
				new String[] { String.valueOf(picture.get_id()) });
		db.close();
		Log.d("deletePicture", picture.toString());
	}


}