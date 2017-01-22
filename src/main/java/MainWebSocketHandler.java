import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by jakub.a.kret@gmail.com on 2017-01-22.
 */
@WebSocket
public class MainWebSocketHandler {
    @OnWebSocketConnect
    public void onConnect(Session user) {
        try {
            updateUserChannelList(user);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        Gson gson = new Gson();
        Request req = gson.fromJson(message, Request.class);
        System.out.println(req);

        if (req.getAction().equals("initializeUser")) {
            App.lobby.put(user, req.getUsername());
            try {
                updateUserChannelList(user);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    private void updateUserChannelList(Session session) throws IOException, JSONException {
        session.getRemote().sendString(String.valueOf(new JSONObject()
                .put("action", "listChannels")
                .put("numberOfChannels", App.channels.size())
                .put("channelNames", App.channelNames)
        ));
    }
}
