package romain.guarnotta.channelmessaging.NetworkManager;

/**
 * Created by romain on 08/02/16.
 */
public interface RequestListener {
    public void onError(String error);
    public void onCompleted(String response);
}


