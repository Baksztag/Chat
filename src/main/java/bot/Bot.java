package bot;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jakub.a.kret@gmail.com on 2017-01-22.
 */
public class Bot {
    private CurrentWeather currentWeather;


    public Bot() {
        this.currentWeather = new CurrentWeather();
    }

    public String answerQuestion(String question) throws IOException {
        String answer;
        switch (question) {
            case ":godzina":
                answer = "Jest godzina " + getTime();
                break;
            case ":dzien":
                answer = "Dzisiaj jest " + getDay();
                break;
            case ":pogoda":
                answer = currentWeather.getCurrentWeather();
                break;
            default:
                answer = "Aby uzyskac odpowiedz wpisz ':godzina', ':dzien' lub ':pogoda'";
                break;
        }

        return answer;
    }

    private String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(new Date());
    }

    private String getDay() {
        return new SimpleDateFormat("EEEE").format(new Date());
    }
}