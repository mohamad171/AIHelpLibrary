package ir.aihelp.library;

/**
 * Created by mohamad on 10/30/2017.
 */

public class ChatModel {
    String username;
    String chattext;
    String datetext;


    public ChatModel(String username, String chattext, String datetext) {
        this.username = username;
        this.chattext = chattext;
        this.datetext = datetext;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChattext() {
        return chattext;
    }

    public void setChattext(String chattext) {
        this.chattext = chattext;
    }

    public String getDatetext() {
        return datetext;
    }

    public void setDatetext(String datetext) {
        this.datetext = datetext;
    }
}
