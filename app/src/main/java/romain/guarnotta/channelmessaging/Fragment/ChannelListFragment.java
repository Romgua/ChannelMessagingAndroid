package romain.guarnotta.channelmessaging.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;

import romain.guarnotta.channelmessaging.Activity.ChannelListActivity;
import romain.guarnotta.channelmessaging.Activity.MessageActivity;
import romain.guarnotta.channelmessaging.Helper.ListViewAdapterForChannel;
import romain.guarnotta.channelmessaging.Model.Channel;
import romain.guarnotta.channelmessaging.Model.ChannelResponse;
import romain.guarnotta.channelmessaging.Network_Manager.ConnexionAsync;
import romain.guarnotta.channelmessaging.Network_Manager.ParseGson;
import romain.guarnotta.channelmessaging.Network_Manager.RequestListener;
import romain.guarnotta.channelmessaging.R;

/**
 * Created by romain on 01/03/16.
 */
public class ChannelListFragment extends Fragment implements RequestListener {
    private ListView lv_channel_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.channel_list_fragment, container);
        lv_channel_list = (ListView)v.findViewById(R.id.lv_channel_list);
        lv_channel_list.setOnItemClickListener((ChannelListActivity)getActivity());
        SharedPreferences settings = getContext().getSharedPreferences(ParseGson.PREFS_NAME, 0);

        String method = "getchannels";
        HashMap<String, String> params = new HashMap<>();
        params.put("accesstoken", ParseGson.getAccessTokenByPrefsFile(settings));

        ConnexionAsync conn = new ConnexionAsync(method, params);
        conn.setRequestListener(this);
        conn.execute();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void startChannelActivity(int position){
        Channel myChannel = (Channel)lv_channel_list.getItemAtPosition(position);

        Intent myChannelActivity =
                new Intent(this.getContext(), MessageActivity.class);
        myChannelActivity.putExtra("channelID", ""+myChannel.getChannelID());
        startActivity(myChannelActivity);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onCompleted(String response) {
        ChannelResponse channelResponse = ParseGson.parseGson(ChannelResponse.class, response);

        // Attach the adapter to a ListView
        lv_channel_list.setAdapter(new ListViewAdapterForChannel(this.getContext(), channelResponse.getChannels()));
    }
}
