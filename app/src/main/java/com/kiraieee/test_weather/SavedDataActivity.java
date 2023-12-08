package com.kiraieee.test_weather;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List; // Import List interface

public class SavedDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_data);

        // Fetch saved data from SQLite
        WeatherDatabaseHelper dbHelper = new WeatherDatabaseHelper(this);
        List<WeatherData> savedWeatherList = dbHelper.getSavedWeatherData(); // Change to List<WeatherData>

        // Convert the List to ArrayList
        ArrayList<WeatherData> savedWeatherArrayList = new ArrayList<>(savedWeatherList);

        // Set up the ListView with a custom adapter
        WeatherAdapter adapter = new WeatherAdapter(this, R.layout.list_item_layout, savedWeatherArrayList);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
