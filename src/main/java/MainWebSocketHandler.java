import bot.Bot;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static j2html.TagCreator.*;


/**
 * Created by jakub.a.kret@gmail.com on 2017-01-22.
 */
@WebSocket
public class MainWebSocketHandler {
    private ChatControls cc = new ChatControls();
    private Bot bot = new Bot();


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
            cc.getLobby().put(user, req.getUsername());
            updateUserChannelList(user);
        }

        if (req.getAction().equals("join")) {
            cc.getLobby().remove(user);
            cc.getChannels().get(req.getChannelID()).put(user, req.getUsername());
            notifyUserJoined(req);
        }

        if (req.getAction().equals("leave")) {
            cc.getChannels().get(req.getOldChannelID()).remove(user);
            cc.getLobby().put(user, req.getUsername());
            updateUserChannelList(user);
            notifyUserLeft(req);
        }

        if (req.getAction().equals("sendMessage")) {
            if (req.getChannelID() != 0) {
                sendMessageToChannel(req);
            } else {
                try {
                    hireChatbot(user, req);
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }

        if (req.getAction().equals("newChannel")) {
            cc.addChannel(req.getChannelName());
            for (Map<Session, String> channel : cc.getChannels()) {
                channel.keySet().stream()
                        .filter(Session::isOpen)
                        .forEach(this::updateUserChannelList);
            }
            cc.getLobby().keySet().stream()
                    .filter(Session::isOpen)
                    .forEach(this::updateUserChannelList);
        }
    }


    private void updateUserChannelList(Session session) {
        try {
            session.getRemote().sendString(String.valueOf(new JSONObject()
                    .put("action", "listChannels")
                    .put("numberOfChannels", cc.getChannels().size())
                    .put("channelNames", cc.getChannelNames())
            ));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void notifyUserJoined(Request req) {
        cc.getChannels().get(req.getChannelID()).keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("action", "join")
                        .put("channelID", req.getChannelID())
                        .put("username", req.getUsername())
                        .put("channelName", cc.getChannelNames().get(req.getChannelID()))
                        .put("userList", cc.getChannels().get(req.getChannelID()).values())
                ));
            } catch (Exception e) {
                System.err.println(e);
            }
        });
    }

    private void notifyUserLeft(Request req) {
        cc.getChannels().get(req.getOldChannelID()).keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("action", "leave")
                        .put("channelID", req.getOldChannelID())
                        .put("username", req.getUsername())
                        .put("userMessage", createHtmlMessageFromSender("SERVER",
                                req.getUsername() + " has left the channel."))
                        .put("userList", cc.getChannels().get(req.getOldChannelID()).values())
                ));
            } catch (Exception e) {
                System.err.println(e);
            }
        });
    }

    private void sendMessageToChannel(Request req) {
        cc.getChannels().get(req.getChannelID()).keySet().stream().filter(Session::isOpen).forEach(session -> {
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

    private void sendMessageToSingleUser(Session user, Request req, String sender, String message) {
        try {
            user.getRemote().sendString(String.valueOf(new JSONObject()
                    .put("action", req.getAction())
                    .put("channelID", req.getChannelID())
                    .put("username", req.getUsername())
                    .put("userMessage", createHtmlMessageFromSender(sender, message))
            ));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void hireChatbot(Session user, Request req) throws IOException {
        sendMessageToSingleUser(user, req, req.getUsername(), req.getUserMessage());
        sendMessageToSingleUser(user, req, "Chatbot", bot.answerQuestion(req.getUserMessage()));
    }

    private String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + " says:"),
                p(message),
                span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date()))
        ).render();
    }
}
