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

    private ListView lv_channel_messages;
    private EditText et_message_writting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_message);

        Button btn_connexion;
        et_message_writting = (EditText)findViewById(R.id.et_message_writting);
        btn_connexion = (Button) findViewById(R.id.btn_send_message);
        btn_connexion.setOnClickListener(this);

        this.getMessages();
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getApplicationContext(),
                "Error getchannels : " + error, Toast.LENGTH_LONG).show();
    }

    private void getMessages() {
        SharedPreferences settings = getSharedPreferences(ParseGson.PREFS_NAME, 0);
        String sChannelID = getIntent().getStringExtra("channelID");

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
        MessageResponse messageResponse = ParseGson.parseGson(MessageResponse.class, response);
        // Attach the adapter to a ListView
        lv_channel_messages = (ListView)findViewById(R.id.lv_channel_messages);
        lv_channel_messages.setAdapter(new ListViewAdapterForMessage(this, messageResponse.getMessages()));

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                getMessages();
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);
    }

    @Override
    public void onClick(View v) {

    }
}
