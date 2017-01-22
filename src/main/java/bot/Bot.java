package bot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jakub.a.kret@gmail.com on 2017-01-22.
 */
public class Bot {
    public String answerQuestion(String question) {
        String answer;
        switch (question) {
            case ":godzina":
                answer = "Jest godzina " + getTime();
                break;
            case ":dzien":
                answer = "Dzisiaj jest " + getDay();
                break;
            case ":pogoda":
                answer = getWeather();
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

    private String getWeather() {
        return "Lekkie zachmurzenie";
    }
}
