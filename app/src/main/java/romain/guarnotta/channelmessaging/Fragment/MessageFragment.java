package romain.guarnotta.channelmessaging.Fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

import romain.guarnotta.channelmessaging.Activity.GPSActivity;
import romain.guarnotta.channelmessaging.Helper.ListViewAdapterForMessage;
import romain.guarnotta.channelmessaging.Model.MessageResponse;
import romain.guarnotta.channelmessaging.Network_Manager.ConnexionAsync;
import romain.guarnotta.channelmessaging.Network_Manager.ParseGson;
import romain.guarnotta.channelmessaging.Network_Manager.RequestListener;
import romain.guarnotta.channelmessaging.R;

/**
 * Created by romain on 01/03/16.
 */
public class MessageFragment extends Fragment
        implements View.OnClickListener, RequestListener {

    private SharedPreferences settings;
    public int iChannelID;
    private ListView lv_channel_messages;
    private EditText et_message_writting;
    Location mLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.message_fragment, container);
        lv_channel_messages = (ListView)v.findViewById(R.id.lv_channel_messages);

        settings = getContext().getSharedPreferences(ParseGson.PREFS_NAME, 0);
        iChannelID = getActivity().getIntent().getIntExtra("channelID", 0);

        Button btn_send_message;
        et_message_writting = (EditText)v.findViewById(R.id.et_message_writting);
        btn_send_message = (Button)v.findViewById(R.id.btn_send_message);
        btn_send_message.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                getMessages();
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);

        this.getMessages();
    }

    private void getMessages() {
        String method = "getmessages";
        HashMap<String, String> params = new HashMap<>();
        params.put("accesstoken", ParseGson.getInfoInPrefsFileByKey(settings, "accesstoken"));
        params.put("channelid", ""+ iChannelID);

        ConnexionAsync conn = new ConnexionAsync(method, params);
        conn.setRequestListener(this);
        conn.execute();
    }

    //getMessages
    @Override
    public void onError(String error) {

    }

    //getMessages
    @Override
    public void onCompleted(String response) {
        if (response.contains("messages")) {
            MessageResponse messageResponse = ParseGson.parseGson(MessageResponse.class, response);
            // Attach the adapter to a ListView
            lv_channel_messages.setAdapter(new ListViewAdapterForMessage(this.getContext(), messageResponse.getMessages()));
            lv_channel_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final CharSequence[] items = {
                            getString(R.string.locate_on_map)
                    };

                    new AlertDialog.Builder(getActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.make_a_choice)
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (0 == which) { // first Item
                                        Toast.makeText(getContext(), "Map", Toast.LENGTH_LONG).show();
                                    }
//                                    else {
                                    //Do some stuff
                                    // But no other items
//                                    }
                                }
                            })
                            .show();
                }
            });
        } else {
            et_message_writting.setText("");
        }
    }

    private void sendMessage() {
        String method = "sendmessage";
        HashMap<String, String> params = new HashMap<>();
        params.put("accesstoken", ParseGson.getInfoInPrefsFileByKey(settings, "accesstoken"));
        params.put("channelid", ""+ iChannelID);
        params.put("message", et_message_writting.getText().toString());
        mLocation = GPSActivity.getCurrentLocation();
        if (null != mLocation) {
            params.put("latitude", ""+mLocation.getLatitude());
            params.put("longitude", ""+mLocation.getLongitude());
        }

        try {
            ConnexionAsync conn = new ConnexionAsync(method, params);
            conn.setRequestListener(this);
            conn.execute();
        } catch (Exception ex) {
            Toast.makeText(this.getContext(), "Error : " + ex, Toast.LENGTH_LONG).show();
        }
    }

    //sendMessage
    @Override
    public void onClick(View v) {
        sendMessage();
    }

    //this
    public void refresh() {
        this.getMessages();
    }
}
