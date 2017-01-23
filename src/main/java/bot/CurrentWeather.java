package bot;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by jakub.a.kret@gmail.com on 2017-01-23.
 */
public class CurrentWeather {
    private String weatherURL;

    public CurrentWeather() {
        this.weatherURL = "http://api.openweathermap.org/data/2.5/weather?"
                + "id=3094802"
                + "&units=metric&APPID=1e44c51bb46a75ea2c1b74fde1e708a9";
    }

    public String getCurrentWeather() throws IOException {
        Weather weather = downloadWeather();
        StringBuilder builder = new StringBuilder();
        return builder
                .append("Pogoda w Krakowie:\n")
                .append("Temperatura: ").append(weather.getTemperature()).append(" stopni Celsjusza, ")
                .append("Cisnienie: ").append(weather.getPressure()).append("hPa, ")
                .append("Wilgotnosc: ").append(weather.getHumidity()).append("%, ")
                .append("Predkosc wiatru: ").append(weather.getWindSpeed()).append("km/h.")
                .toString();
    }

    private Weather downloadWeather() throws IOException {
        Gson gson = new Gson();
        URL url = new URL(weatherURL);
        return gson.fromJson(new JsonReader(new InputStreamReader(url.openStream())), Weather.class);
    }
}
