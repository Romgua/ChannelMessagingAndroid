package romain.guarnotta.channelmessaging;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by romain on 08/02/16.
 */
public class ChannelListActivity extends Activity implements View.OnClickListener, RequestListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_channel);

        SharedPreferences settings = getSharedPreferences(ParseGson.PREFS_NAME, 0);

        String method = "getchannels";
        HashMap<String, String> params = new HashMap<>();
        params.put("accesstoken", ParseGson.getAccessTokenByPrefsFile(settings));
        ConnexionAsync conn = new ConnexionAsync(method, params);
        conn.setRequestListener(this);
        conn.execute();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onError(String error) {
        Toast.makeText(getApplicationContext(),
                "Error getchannels : " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCompleted(String response) {
        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
        ChannelResponse channelResponse = ParseGson.parseGson(ChannelResponse.class, response);

        // Attach the adapter to a ListView
        ListView lv_channel_list = (ListView)findViewById(R.id.lv_channel_list);
        lv_channel_list.setAdapter(new ListViewAdapter(this, channelResponse.getChannels()));
    }
}
