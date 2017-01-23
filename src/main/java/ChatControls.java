import org.eclipse.jetty.websocket.api.Session;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jakub.a.kret@gmail.com on 2017-01-23.
 */
public class ChatControls {
    private List<Map<Session, String>> channels;
    private List<String> channelNames;
    private Map<Session, String> lobby;


    public ChatControls() {
        this.channels = new LinkedList<>();
        this.channelNames = new LinkedList<>();
        this.lobby = new ConcurrentHashMap<>();
        Map<Session, String> chatbot = new ConcurrentHashMap<>();
        this.channels.add(chatbot);
        this.channelNames.add("Chatbot");
    }

    public void addChannel(String channelName) {
        channels.add(new ConcurrentHashMap<>());
        channelNames.add(channelName);
    }

    public List<Map<Session, String>> getChannels() {
        return channels;
    }

    public List<String> getChannelNames() {
        return channelNames;
    }

    public Map<Session, String> getLobby() {
        return lobby;
    }
}
