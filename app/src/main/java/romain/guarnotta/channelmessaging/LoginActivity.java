package romain.guarnotta.channelmessaging;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.ConnectException;
import java.util.HashMap;

public class LoginActivity extends Activity implements View.OnClickListener, RequestListener {
    private EditText et_id;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);

        Button btn_connexion;
        et_id = (EditText)findViewById(R.id.et_id);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_connexion = (Button) findViewById(R.id.btn_connexion);
        btn_connexion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String method = "connect";
        HashMap<String, String> params = new HashMap<>();
        params.put("username",et_id.getText().toString());
        params.put("password", et_password.getText().toString());

        ConnexionAsync conn = new ConnexionAsync(method, params);
        conn.setRequestListener(this);
        conn.execute();
    }

    private void startChannelListActivity(){
        Intent myChannelListActivity =
                new Intent(this, ChannelListActivity.class);
        startActivity(myChannelListActivity);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getApplicationContext(),
                "Error connection : "+error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCompleted(String response) {
        try {
            ParseGson.stockAccessTokenToPrefsFile(this, response);
            startChannelListActivity();
        } catch (ConnectException ex) {
            onError(ex.getMessage());
        }
    }

}