package romain.guarnotta.channelmessaging.Model;

/**
 * Created by romain on 08/02/16.
 */
public class ConnectResponse {
    private String response = "";
    private String code = "";
    private String accesstoken = "";

    ConnectResponse(){}

    public String getResponse() {
        return response;
    }

    public String getCode() {
        return code;
    }

    public String getAccesstoken() {
        return accesstoken;
    }
}
