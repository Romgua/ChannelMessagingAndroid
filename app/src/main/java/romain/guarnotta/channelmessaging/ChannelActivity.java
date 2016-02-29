package romain.guarnotta.channelmessaging;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by romain on 08/02/16.
 */
public class ChannelActivity extends Activity implements RequestListener {

    private SharedPreferences settings = getSharedPreferences(ParseGson.PREFS_NAME, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String method = "getchannels";
        // recup ID channel avec depuis le putExtra

        HashMap<String, String> params = new HashMap<>();
        params.put("accesstoken", ParseGson.getAccessTokenByPrefsFile(settings));
//        params.put("channelID", ""+myChannel.getChannelID());
        ConnexionAsync conn = new ConnexionAsync(method, params);
        conn.setRequestListener(this);
        conn.execute();
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getApplicationContext(),
                "Error getchannels : " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCompleted(String response) {

    }
}
