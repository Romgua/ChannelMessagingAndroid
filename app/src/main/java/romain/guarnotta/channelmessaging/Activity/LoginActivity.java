package romain.guarnotta.channelmessaging.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.ConnectException;
import java.util.HashMap;

import romain.guarnotta.channelmessaging.Network_Manager.ConnexionAsync;
import romain.guarnotta.channelmessaging.Network_Manager.ParseGson;
import romain.guarnotta.channelmessaging.R;
import romain.guarnotta.channelmessaging.Network_Manager.RequestListener;

public class LoginActivity extends NotificationActivity
        implements View.OnClickListener, RequestListener {

    private static Context context;
    private EditText et_id;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.content_login);

        SharedPreferences settings = this.getSharedPreferences(ParseGson.PREFS_NAME, 0);

        Button btn_connexion;
        et_id = (EditText)findViewById(R.id.et_id);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_connexion = (Button) findViewById(R.id.btn_connexion);
        btn_connexion.setOnClickListener(this);

        if (!ParseGson.getInfoInPrefsFileByKey(settings, "accesstoken").equalsIgnoreCase("error")) {
            et_id.setText(ParseGson.getInfoInPrefsFileByKey(settings, "username"));
            et_password.setText(ParseGson.getInfoInPrefsFileByKey(settings, "password"));
            logIn();
        }
    }

    @Override
    public void onClick(View v) {
        logIn();
    }

    private void logIn() {
        String method = "connect";
        HashMap<String, String> params = new HashMap<>();
        params.put("username", et_id.getText().toString());
        params.put("password", et_password.getText().toString());
        params.put("registrationid", getRegistrationId());

        ConnexionAsync conn = new ConnexionAsync(method, params);
        conn.setRequestListener(this);
        conn.execute();
    }

    public static void logOut() {
        ParseGson.removePreferencesByKey(LoginActivity.getAppContext(), "accesstoken");
        ParseGson.removePreferencesByKey(LoginActivity.getAppContext(), "username");
        ParseGson.removePreferencesByKey(LoginActivity.getAppContext(), "password");
        ParseGson.removePreferencesByKey(LoginActivity.getAppContext(), "registrationid");
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
            ParseGson.stockPreferences(this, "username", et_id.getText().toString());
            ParseGson.stockPreferences(this, "password", et_password.getText().toString());
            ParseGson.stockPreferences(this, "registrationid", getRegistrationId());
            startChannelListActivity();
        } catch (ConnectException ex) {
            onError(ex.getMessage());
        }
    }

    public static Context getAppContext() {
        return LoginActivity.context;
    }

}