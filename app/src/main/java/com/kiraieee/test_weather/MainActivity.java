package com.kiraieee.test_weather;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private Button buttonGetWeather;
    private TextView textViewWeather;
    private ImageView imageViewWeather;
    private TextView textViewDescription;
    private TextView textViewHumidity;
    private TextView textViewWindSpeed;

    private SharedPreferences sharedPreferences;

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
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, WeatherInfo> {

        @Override
        protected WeatherInfo doInBackground(String... params) {
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

                    return new WeatherInfo(temperature, imageUrl, description, humidity, windSpeed);
                } else {
                    return null; // Handle the case when information is not available
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null; // Handle the case when an error occurs
            }
        }

        @Override
        protected void onPostExecute(WeatherInfo result) {
            if (result != null) {
                textViewWeather.setText(result.getTemperature());
                textViewDescription.setText(result.getDescription());
                textViewHumidity.setText(result.getHumidity());
                textViewWindSpeed.setText(result.getWindSpeed());

                // Load the image using Picasso
                Picasso.get().load(result.getImageUrl()).into(imageViewWeather);

                // Make the ImageView visible
                imageViewWeather.setVisibility(View.VISIBLE);
            } else {
                // Handle the case when information is not available or an error occurs
                textViewWeather.setText("NOT FOUND");
            }
        }
    }
}
