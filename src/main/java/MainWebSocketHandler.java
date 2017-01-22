import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static j2html.TagCreator.*;


/**
 * Created by jakub.a.kret@gmail.com on 2017-01-22.
 */
@WebSocket
public class MainWebSocketHandler {
    @OnWebSocketConnect
    public void onConnect(Session user) {
        updateUserChannelList(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        Gson gson = new Gson();
        Request req = gson.fromJson(message, Request.class);
        System.out.println(req);

        if (req.getAction().equals("initializeUser")) {
            App.lobby.put(user, req.getUsername());
            updateUserChannelList(user);
        }

        if (req.getAction().equals("join")) {
            App.lobby.remove(user);
            App.channels.get(req.getChannelID()).put(user, req.getUsername());
            notifyUserJoined(req);
        }

        if (req.getAction().equals("leave")) {
            App.channels.get(req.getOldChannelID()).remove(user);
            App.lobby.put(user, req.getUsername());
            updateUserChannelList(user);
            notifyUserLeft(req);
        }

        if (req.getAction().equals("sendMessage")) {
            sendMessageToChannel(req);
        }

        if (req.getAction().equals("newChannel")) {
            App.addChannel(req.getChannelName());
            for (Map<Session, String> channel : App.channels) {
                channel.keySet().stream()
                        .filter(Session::isOpen)
                        .forEach(this::updateUserChannelList);
            }
            App.lobby.keySet().stream()
                    .filter(Session::isOpen)
                    .forEach(this::updateUserChannelList);
        }
    }


    private void updateUserChannelList(Session session) {
        try {
            session.getRemote().sendString(String.valueOf(new JSONObject()
                    .put("action", "listChannels")
                    .put("numberOfChannels", App.channels.size())
                    .put("channelNames", App.channelNames)
            ));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void notifyUserJoined(Request req) {
        App.channels.get(req.getChannelID()).keySet().stream().filter(Session::isOpen).forEach(session -> {
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

    private void notifyUserLeft(Request req) {
        App.channels.get(req.getOldChannelID()).keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("action", "leave")
                        .put("channelID", req.getOldChannelID())
                        .put("username", req.getUsername())
                        .put("userMessage", createHtmlMessageFromSender("SERVER",
                                req.getUsername() + " has left the channel."))
                        .put("userList", App.channels.get(req.getOldChannelID()).values())
                ));
            } catch (Exception e) {
                System.err.println(e);
            }
        });
    }

    private void sendMessageToChannel(Request req) {
        App.channels.get(req.getChannelID()).keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("action", req.getAction())
                        .put("channelID", req.getChannelID())
                        .put("username", req.getUsername())
                        .put("userMessage", createHtmlMessageFromSender(req.getUsername(),
                                req.getUserMessage()))
                ));
            } catch (Exception e) {
                System.err.println(e);
            }
        });
    }

    private String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + " says:"),
                p(message),
                span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date()))
        ).render();
    }
}
