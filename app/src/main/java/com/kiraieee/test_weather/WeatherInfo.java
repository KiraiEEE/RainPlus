package com.kiraieee.test_weather;

public class WeatherInfo {

    private String temperature;
    private String imageUrl;
    private String description;
    private String humidity;
    private String windSpeed;

    public WeatherInfo(String temperature, String imageUrl, String description, String humidity, String windSpeed) {
        this.temperature = temperature;
        this.imageUrl = imageUrl;
        this.description = description;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    public String getTemperature() {
        return temperature +"°C";
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getHumidity() {
        return "Humidity : "+humidity;
    }

    public String getWindSpeed() {
        return "Wind Speed : "+windSpeed;
    }
}
