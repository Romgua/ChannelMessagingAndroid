package romain.guarnotta.channelmessaging.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

import romain.guarnotta.channelmessaging.Helper.ListViewAdapterForMessage;
import romain.guarnotta.channelmessaging.Model.MessageResponse;
import romain.guarnotta.channelmessaging.Network_Manager.ConnexionAsync;
import romain.guarnotta.channelmessaging.Network_Manager.ParseGson;
import romain.guarnotta.channelmessaging.Network_Manager.RequestListener;
import romain.guarnotta.channelmessaging.R;

/**
 * Created by romain on 01/03/16.
 */
public class MessageFragment extends Fragment implements View.OnClickListener, RequestListener {

    private SharedPreferences settings;
    public int sChannelID;
    private ListView lv_channel_messages;
    private EditText et_message_writting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.message_fragment, container);
        lv_channel_messages = (ListView)v.findViewById(R.id.lv_channel_messages);

        settings = getContext().getSharedPreferences(ParseGson.PREFS_NAME, 0);
        sChannelID = getActivity().getIntent().getIntExtra("channelID", 0);

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                getMessages();
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);

        Button btn_send_message;
        et_message_writting = (EditText)v.findViewById(R.id.et_message_writting);
        btn_send_message = (Button)v.findViewById(R.id.btn_send_message);
        btn_send_message.setOnClickListener(this);

        this.getMessages();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getMessages() {
        String method = "getmessages";
        HashMap<String, String> params = new HashMap<>();
        params.put("accesstoken", ParseGson.getAccessTokenByPrefsFile(settings));
        params.put("channelid", ""+sChannelID);

        ConnexionAsync conn = new ConnexionAsync(method, params);
        conn.setRequestListener(this);
        conn.execute();
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onCompleted(String response) {
        if (response.contains("messages")) {
            MessageResponse messageResponse = ParseGson.parseGson(MessageResponse.class, response);
            // Attach the adapter to a ListView
            lv_channel_messages.setAdapter(new ListViewAdapterForMessage(this.getContext(), messageResponse.getMessages()));
        } else {
            et_message_writting.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        String method = "sendmessage";
        HashMap<String, String> params = new HashMap<>();
        params.put("accesstoken", ParseGson.getAccessTokenByPrefsFile(settings));
        params.put("channelid", ""+sChannelID);
        params.put("message", et_message_writting.getText().toString());

        try {
            ConnexionAsync conn = new ConnexionAsync(method, params);
            conn.setRequestListener(this);
            conn.execute();
        } catch (Exception ex) {
            Toast.makeText(this.getContext(), "Error : " + ex, Toast.LENGTH_LONG).show();
        }
    }

    public void refresh() {
        this.getMessages();
    }
}
