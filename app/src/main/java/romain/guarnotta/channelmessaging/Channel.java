package romain.guarnotta.channelmessaging;

/**
 * Created by romain on 08/02/16.
 */
public class Channel {
    private int channelID = 0;
    private String name = "";
    private int connecteusers = 0;


    public int getChannelID() {
        return channelID;
    }

    public String getName() {
        return name;
    }

    public int getConnecteusers() {
        return connecteusers;
    }
}
