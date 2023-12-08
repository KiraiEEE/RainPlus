package com.kiraieee.test_weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.content.Context;


import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private Button buttonGetWeather;
    private TextView textViewWeather;
    private ImageView imageViewWeather;
    private TextView textViewDescription;
    private TextView textViewHumidity;
    private TextView textViewWindSpeed;

    private SharedPreferences sharedPreferences;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        buttonGetWeather = findViewById(R.id.buttonGetWeather);
        textViewWeather = findViewById(R.id.textViewWeather);
        imageViewWeather = findViewById(R.id.imageViewWeather);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewHumidity = findViewById(R.id.textViewHumidity);
        textViewWindSpeed = findViewById(R.id.textViewWindSpeed);

        // Initialize SharedPreferences
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        // Check if there is a saved location
        String savedLocation = sharedPreferences.getString("location", "");
        if (!savedLocation.isEmpty()) {
            editTextCity.setText(savedLocation);
            new FetchWeatherTask().execute(savedLocation);
        }

        buttonGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = editTextCity.getText().toString();
                new FetchWeatherTask().execute(location);
            }
        });

        // Add menu icon
        ImageView menuIcon = findViewById(R.id.menuIcon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveWeatherData();
            }
        });
    }
    private void saveWeatherData() {
        // Get the current weather data
        String city = editTextCity.getText().toString();
        String weather = textViewDescription.getText().toString();
        String temperature = textViewWeather.getText().toString();
        String windSpeed = textViewWindSpeed.getText().toString();
        String humidity = textViewHumidity.getText().toString();
        String date = getCurrentDate(); // Implement a method to get the current date

        // Save the data to SQLite
        WeatherDatabaseHelper dbHelper = new WeatherDatabaseHelper(this);
        dbHelper.saveWeatherData(new WeatherData(city, weather, temperature, windSpeed, humidity, date));
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }


    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About")
                .setMessage("\nAkram Ben Fekih\n\nhttps://github.com/KiraiEEE")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());



        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_view_saved_data) {

                    Intent intent = new Intent(MainActivity.this, SavedDataActivity.class);
                    startActivity(intent);

                    return true;
                } else if (itemId == R.id.menu_about) {

                    showAboutDialog();

                    return true;
                } else {
                    return false;
                }
            }

        });

        popupMenu.show();
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, Bundle> {
        @Override
        protected Bundle doInBackground(String... params) {
            // Fetch weather information as before
            String cityName = params[0];
            String weatherUrl = "https://www.google.com/search?q=" + cityName + "+weather&hl=en";

            try {
                Document document = Jsoup.connect(weatherUrl).get();
                Element temperatureElement = document.getElementById("wob_tm");
                Element imageElement = document.getElementById("wob_tci");
                Element descriptionElement = document.getElementById("wob_dc");
                Element humidityElement = document.getElementById("wob_hm");
                Element windSpeedElement = document.getElementById("wob_ws");

                if (temperatureElement != null && imageElement != null
                        && descriptionElement != null && humidityElement != null && windSpeedElement != null) {
                    String temperature = temperatureElement.text();
                    String imageUrl = imageElement.absUrl("src");
                    String description = descriptionElement.text();
                    String humidity = humidityElement.text();
                    String windSpeed = windSpeedElement.text();

                    // Save the location in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("location", cityName);
                    editor.apply();

                    // Create a Bundle to hold the weather information
                    Bundle weatherBundle = new Bundle();
                    weatherBundle.putString("temperature", temperature);
                    weatherBundle.putString("imageUrl", imageUrl);
                    weatherBundle.putString("description", description);
                    weatherBundle.putString("humidity", humidity);
                    weatherBundle.putString("windSpeed", windSpeed);


                    return weatherBundle;
                } else {
                    return null; // Handle the case when information is not available
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null; // Handle the case when an error occurs
            }
        }

        @Override
        protected void onPostExecute(Bundle result) {
            if (result != null) {
                textViewWeather.setText(result.getString("temperature"));
                textViewDescription.setText(result.getString("description"));
                textViewHumidity.setText(result.getString("humidity"));
                textViewWindSpeed.setText(result.getString("windSpeed"));

                // Load the image using Picasso
                Picasso.get().load(result.getString("imageUrl")).into(imageViewWeather);

                // Make the ImageView visible
                imageViewWeather.setVisibility(View.VISIBLE);
            } else {
                // Handle the case when information is not available or an error occurs
                textViewWeather.setText("NOT FOUND");
            }
        }
    }
}
