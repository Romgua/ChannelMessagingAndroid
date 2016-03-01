package romain.guarnotta.channelmessaging.Model;

/**
 * Created by romain on 08/02/16.
 */
public class Channel {
    private int channelID = 0;
    private String name = "";
    private int connectedusers = 0;


    public int getChannelID() { return channelID; }

    public String getName() { return name; }

    public int getConnectedusers() { return connectedusers; }
}
