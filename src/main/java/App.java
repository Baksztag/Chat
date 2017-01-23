import org.eclipse.jetty.websocket.api.Session;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
