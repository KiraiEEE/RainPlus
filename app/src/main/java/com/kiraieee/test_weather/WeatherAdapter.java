package com.kiraieee.test_weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class WeatherAdapter extends ArrayAdapter<WeatherData> {
    private int layoutResource;

    public WeatherAdapter(Context context, int resource, ArrayList<WeatherData> weatherList) {
        super(context, resource, weatherList);
        layoutResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(layoutResource, null);
        }

        WeatherData weatherData = getItem(position);

        if (weatherData != null) {
            TextView cityTextView = view.findViewById(R.id.textCity);
            TextView weatherTextView = view.findViewById(R.id.textWeather);
            TextView temperatureTextView = view.findViewById(R.id.textTemperature);
            TextView windSpeedTextView = view.findViewById(R.id.textWindSpeed);
            TextView humidityTextView = view.findViewById(R.id.textHumidity);
            TextView dateTextView = view.findViewById(R.id.textDate);

            if (cityTextView != null) {
                cityTextView.setText("City: " + weatherData.getCity());
            }

            if (weatherTextView != null) {
                weatherTextView.setText("Weather: " + weatherData.getWeather());
            }

            if (temperatureTextView != null) {
                temperatureTextView.setText("Temperature: " + weatherData.getTemperature());
            }

            if (windSpeedTextView != null) {
                windSpeedTextView.setText("Wind Speed: " + weatherData.getWindSpeed());
            }

            if (humidityTextView != null) {
                humidityTextView.setText("Humidity: " + weatherData.getHumidity());
            }

            if (dateTextView != null) {
                dateTextView.setText("Date: " + weatherData.getDate());
            }
        }

        return view;
    }
}
