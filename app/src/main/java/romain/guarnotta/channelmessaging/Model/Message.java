package romain.guarnotta.channelmessaging.Model;

/**
 * Created by romain on 29/02/16.
 */
public class Message {
    private int userID = 0;
    private String message = "";
    private String date = "";
    private String imageUrl = "";
    private Double latitude = 0.0;
    private Double longitude = 0.0;

    public int getUserID() { return userID; }

    public String getMessage() { return message; }

    public String getDate() { return date; }

    public String getImageUrl() { return imageUrl; }

    public Double getLatitude() { return latitude; }

    public Double getLongitude() { return longitude; }
}
