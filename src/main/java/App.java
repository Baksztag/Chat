import static spark.Spark.*;

/**
 * Created by jakub.a.kret@gmail.com on 2017-01-22.
 */
public class App {
    public static void main(String[] args) {
        staticFileLocation("/public");
        webSocket("/main", MainWebSocketHandler.class);
        init();
    }
}
