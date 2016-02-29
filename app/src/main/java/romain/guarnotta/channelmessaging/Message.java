package romain.guarnotta.channelmessaging;

/**
 * Created by romain on 29/02/16.
 */
public class Message {
    private int userID = 0;
    private String message = "";
    private String date = "";
    private String imageUrl = "";

    public int getUserID() { return userID; }

    public String getMessage() { return message; }

    public String getDate() { return date; }

    public String getImageUrl() { return imageUrl; }
}
