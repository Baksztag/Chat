/**
 * Created by jakub.a.kret@gmail.com on 2017-01-22.
 */
public class Request {
    private String action;
    private String username;
    private int channelID;
    private int oldChannelID;
    private String userMessage;
    private String channelName;


    @Override
    public String toString() {
        return "Request{" +
                "action='" + action + '\'' +
                ", username='" + username + '\'' +
                ", channelID=" + channelID +
                ", oldChannelID=" + oldChannelID +
                ", userMessage='" + userMessage + '\'' +
                ", channelName='" + channelName + '\'' +
                '}';
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public int getOldChannelID() {
        return oldChannelID;
    }

    public void setOldChannelID(int oldChannelID) {
        this.oldChannelID = oldChannelID;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
