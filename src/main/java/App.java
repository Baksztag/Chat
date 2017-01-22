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
    static List<Map<Session, String>> channels = new LinkedList<Map<Session, String>>();
    static List<String> channelNames = new LinkedList<>();
    static Map<Session, String> lobby = new ConcurrentHashMap<Session, String>();

    public static void main(String[] args) {
        Map<Session, String> general = new ConcurrentHashMap<Session, String>();
        channels.add(general);
        channelNames.add("General");
        staticFileLocation("/public");
        webSocket("/main", MainWebSocketHandler.class);
        init();
    }
}
