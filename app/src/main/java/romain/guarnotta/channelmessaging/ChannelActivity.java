package romain.guarnotta.channelmessaging;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by romain on 08/02/16.
 */
public class ChannelActivity extends Activity implements View.OnClickListener, RequestListener {

    private SharedPreferences settings;
    private String sChannelID;
    private EditText et_message_writting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_message);

        settings = getSharedPreferences(ParseGson.PREFS_NAME, 0);
        sChannelID = getIntent().getStringExtra("channelID");

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                getMessages();
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);

        Button btn_send_message;
        et_message_writting = (EditText)findViewById(R.id.et_message_writting);
        btn_send_message = (Button) findViewById(R.id.btn_send_message);
        btn_send_message.setOnClickListener(this);

        this.getMessages();
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getApplicationContext(),
                "Error getchannels : " + error, Toast.LENGTH_LONG).show();
    }

    private void getMessages() {
        String method = "getmessages";
        HashMap<String, String> params = new HashMap<>();
        params.put("accesstoken", ParseGson.getAccessTokenByPrefsFile(settings));
        params.put("channelid", sChannelID);

        ConnexionAsync conn = new ConnexionAsync(method, params);
        conn.setRequestListener(this);
        conn.execute();
    }

    @Override
    public void onCompleted(String response) {
        if (response.contains("messages")) {
            MessageResponse messageResponse = ParseGson.parseGson(MessageResponse.class, response);
            // Attach the adapter to a ListView
            ListView lv_channel_messages = (ListView) findViewById(R.id.lv_channel_messages);
            lv_channel_messages.setAdapter(new ListViewAdapterForMessage(this, messageResponse.getMessages()));
        } else {
            et_message_writting.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        String method = "sendmessage";
        HashMap<String, String> params = new HashMap<>();
        params.put("accesstoken", ParseGson.getAccessTokenByPrefsFile(settings));
        params.put("channelid", sChannelID);
        params.put("message", et_message_writting.getText().toString());

        try {
            ConnexionAsync conn = new ConnexionAsync(method, params);
            conn.setRequestListener(this);
            conn.execute();
        } catch (Exception ex) {
            Toast.makeText(this, "Error : "+ex, Toast.LENGTH_LONG).show();
        }
    }
}
