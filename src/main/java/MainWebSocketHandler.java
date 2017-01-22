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

        if (req.getAction().equals("join")) {
            App.lobby.remove(user);
            App.channels.get(req.getChannelID()).put(user, req.getUsername());
            try {
                notifyUsers(req, req.getChannelID());
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

    private void notifyUsers(Request req, int channelID) {
        App.channels.get(channelID).keySet().stream().filter(Session::isOpen).forEach(session -> {
            System.out.println(App.channels.get(req.getChannelID()).values());
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("action", "join")
                        .put("channelID", req.getChannelID())
                        .put("username", req.getUsername())
                        .put("channelName", App.channelNames.get(req.getChannelID()))
                        .put("userList", App.channels.get(req.getChannelID()).values())
                ));
            } catch (Exception e) {
                System.err.println(e);
            }
        });
    }
}
