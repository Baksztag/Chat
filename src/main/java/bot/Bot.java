package bot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jakub.a.kret@gmail.com on 2017-01-22.
 */
public class Bot {
    public String answerQuestion(String question) {
        String answer = "";
        if(question.equals(":godzina")) {
            answer = "Jest godzina " + getTime();
        }
        else if(question.equals(":dzien")) {
            answer = "Dzisiaj jest " + getDay();
        }
        else if(question.equals(":pogoda")) {
            answer = getWeather();
        }
        else {
            answer = "Aby uzyskac odpowiedz wpisz ':godzina', ':dzien' lub ':pogoda'";
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
        return null;
    }
}
