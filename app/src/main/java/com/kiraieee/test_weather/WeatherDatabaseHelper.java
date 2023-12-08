package com.kiraieee.test_weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class WeatherDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "weather_data";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_WEATHER = "weather";
    private static final String COLUMN_TEMPERATURE = "temperature";
    private static final String COLUMN_WIND_SPEED = "wind_speed";
    private static final String COLUMN_HUMIDITY = "humidity";
    private static final String COLUMN_DATE = "date";

    public WeatherDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CITY + " TEXT, " +
                COLUMN_WEATHER + " TEXT, " +
                COLUMN_TEMPERATURE + " TEXT, " +
                COLUMN_WIND_SPEED + " TEXT, " +
                COLUMN_HUMIDITY + " TEXT, " +
                COLUMN_DATE + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema upgrades if needed
    }

    public void saveWeatherData(WeatherData weatherData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_CITY, weatherData.getCity());
        values.put(COLUMN_WEATHER, weatherData.getWeather());
        values.put(COLUMN_TEMPERATURE, weatherData.getTemperature());
        values.put(COLUMN_WIND_SPEED, weatherData.getWindSpeed());
        values.put(COLUMN_HUMIDITY, weatherData.getHumidity());
        values.put(COLUMN_DATE, weatherData.getDate());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<WeatherData> getSavedWeatherData() {
        List<WeatherData> weatherDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String city = cursor.getString(cursor.getColumnIndex(COLUMN_CITY));
                String weather = cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER));
                String temperature = cursor.getString(cursor.getColumnIndex(COLUMN_TEMPERATURE));
                String windSpeed = cursor.getString(cursor.getColumnIndex(COLUMN_WIND_SPEED));
                String humidity = cursor.getString(cursor.getColumnIndex(COLUMN_HUMIDITY));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));

                WeatherData weatherData = new WeatherData(city, weather, temperature, windSpeed, humidity, date);
                weatherDataList.add(weatherData);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return weatherDataList;
    }
}
