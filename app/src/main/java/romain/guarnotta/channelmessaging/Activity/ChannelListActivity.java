package romain.guarnotta.channelmessaging.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.HashMap;

import romain.guarnotta.channelmessaging.Network_Manager.ConnexionAsync;
import romain.guarnotta.channelmessaging.Helper.ListViewAdapterForChannel;
import romain.guarnotta.channelmessaging.Model.Channel;
import romain.guarnotta.channelmessaging.Model.ChannelResponse;
import romain.guarnotta.channelmessaging.Network_Manager.ParseGson;
import romain.guarnotta.channelmessaging.R;
import romain.guarnotta.channelmessaging.Network_Manager.RequestListener;

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

    private void startChannelActivity(int position){
        Channel myChannel = (Channel)lv_channel_list.getItemAtPosition(position);

        Intent myChannelActivity =
                new Intent(this, ChannelActivity.class);
        myChannelActivity.putExtra("channelID", ""+myChannel.getChannelID());
        startActivity(myChannelActivity);
    }

    @Override
    public void onCompleted(String response) {
        ChannelResponse channelResponse = ParseGson.parseGson(ChannelResponse.class, response);

        // Attach the adapter to a ListView
        lv_channel_list = (ListView)findViewById(R.id.lv_channel_list);
        lv_channel_list.setAdapter(new ListViewAdapterForChannel(this, channelResponse.getChannels()));
        lv_channel_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    startChannelActivity(position);
                } catch (Exception ex) {
                    onError(ex.getMessage());
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
