package romain.guarnotta.channelmessaging;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by romain on 08/02/16.
 */
public class ChannelListActivity extends Activity implements AdapterView.OnItemClickListener, RequestListener {

    private ListView lv_channel_list;

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
    public void onError(String error) {
        Toast.makeText(getApplicationContext(),
                "Error getchannels : " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCompleted(String response) {
        ChannelResponse channelResponse = ParseGson.parseGson(ChannelResponse.class, response);

        // Attach the adapter to a ListView
        lv_channel_list = (ListView)findViewById(R.id.lv_channel_list);
        lv_channel_list.setAdapter(new ListViewAdapter(this, channelResponse.getChannels()));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Channel myChannel = (Channel)lv_channel_list.getItemAtPosition(position);

        Intent myChannelActivity =
                new Intent(this, ChannelActivity.class);
        myChannelActivity.putExtra("channelID", myChannel.getChannelID());
        startActivity(myChannelActivity);
    }

}
