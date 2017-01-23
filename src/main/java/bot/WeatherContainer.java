package bot;

/**
 * Created by jakub.a.kret@gmail.com on 2017-01-23.
 */
public class WeatherContainer {
    private MainBean main;
    private WindBean wind;


    public int getTemperature() {
        return main.getTemp();
    }

    public int getPressure() {
        return main.getPressure();
    }

    public int getHumidity() {
        return main.getHumidity();
    }

    public double getWindSpeed() {
        return wind.getSpeed();
    }

    public MainBean getMain() {
        return main;
    }

    public void setMain(MainBean main) {
        this.main = main;
    }

    public WindBean getWind() {
        return wind;
    }

    public void setWind(WindBean wind) {
        this.wind = wind;
    }


    public static class MainBean {
        /**
         * temp : 0
         * pressure : 1023
         * humidity : 72
         * temp_min : 0
         * temp_max : 0
         */

        private int temp;
        private int pressure;
        private int humidity;
        private int temp_min;
        private int temp_max;

        public int getTemp() {
            return temp;
        }

        public void setTemp(int temp) {
            this.temp = temp;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public int getTemp_min() {
            return temp_min;
        }

        public void setTemp_min(int temp_min) {
            this.temp_min = temp_min;
        }

        public int getTemp_max() {
            return temp_max;
        }

        public void setTemp_max(int temp_max) {
            this.temp_max = temp_max;
        }
    }

    public static class WindBean {
        /**
         * speed : 1.5
         * deg : 230
         */

        private double speed;

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }
    }
}
