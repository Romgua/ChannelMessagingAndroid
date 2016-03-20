package romain.guarnotta.channelmessaging.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.ConnectException;
import java.util.HashMap;

import romain.guarnotta.channelmessaging.NetworkManager.ConnexionAsync;
import romain.guarnotta.channelmessaging.NetworkManager.ParseGson;
import romain.guarnotta.channelmessaging.R;
import romain.guarnotta.channelmessaging.NetworkManager.RequestListener;

public class LoginActivity extends NotificationActivity
        implements View.OnClickListener, RequestListener {

    private static Context context;
    private EditText et_id;
    private EditText et_password;
    private String action = "";
    private SharedPreferences settings;
    private Boolean isCheckAccesstokenValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.content_login);

        settings = this.getSharedPreferences(ParseGson.PREFS_NAME, 0);

        Button btn_connexion;
        et_id = (EditText)findViewById(R.id.et_id);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_connexion = (Button) findViewById(R.id.btn_connexion);
        btn_connexion.setOnClickListener(this);

        isAccesstokenValid();
    }

    @Override
    public void onClick(View v) {
        if (et_id.getText().toString().equals("") && et_password.getText().toString().equals("")) {
            onError("Please enter your login and password");
        } else if (et_id.getText().toString().equals("")) {
            onError("Please enter your login");
        } else if (et_password.getText().toString().equals("")) {
            onError("Please enter your password");
        } else {
            logIn();
        }
    }

    private void logIn() {
        try {
            String method = "connect";
            HashMap<String, String> params = new HashMap<>();
            params.put("username", et_id.getText().toString());
            params.put("password", et_password.getText().toString());
            params.put("registrationid", getRegistrationId());

            ConnexionAsync conn = new ConnexionAsync(method, params);
            conn.setRequestListener(this);
            conn.execute();
        } catch (Exception e) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void isAccesstokenValid() {
        if (!ParseGson.getInfoInPrefsFileByKey(settings, "accesstoken").equalsIgnoreCase("error")) {
            try {
                isCheckAccesstokenValid = true;

                String method = "isaccesstokenvalid";
                HashMap<String, String> params = new HashMap<>();
                params.put("accesstoken", ParseGson.getInfoInPrefsFileByKey(settings, "accesstoken"));

                ConnexionAsync conn = new ConnexionAsync(method, params);
                conn.setRequestListener(this);
                conn.execute();
            } catch (Exception e) {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        } else {
            isCheckAccesstokenValid = false;
        }
    }

    public static void logOut() {
        ParseGson.removePreferencesByKey(LoginActivity.getAppContext(), "accesstoken");
        ParseGson.removePreferencesByKey(LoginActivity.getAppContext(), "registrationid");
    }

    private void startChannelListActivity(){
        Intent myChannelListActivity =
                new Intent(this, ChannelListActivity.class);

        if (getIntent().getAction().equals("showByNotification")) {
            String channelID = getIntent().getStringExtra("channelid");
            myChannelListActivity.putExtra("channelID", channelID);
        }

        startActivity(myChannelListActivity);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getApplicationContext(),
                error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCompleted(String response) {
        try {
            // checked accesstoken but is not ok
            if (isCheckAccesstokenValid && !ParseGson.getCode(response).equals("200")) {
                Toast.makeText(this,
                        "Error connection",
                        Toast.LENGTH_LONG).show();
            } else {
                // no cheched accesstoken
                if (!isCheckAccesstokenValid) {
                    ParseGson.stockAccessTokenToPrefsFile(this, response);
                    ParseGson.stockPreferences(this, "registrationid", getRegistrationId());
                }
                startChannelListActivity();
            }
        } catch (ConnectException ex) {
            onError(ex.getMessage());
        }
    }

    public static Context getAppContext() {
        return LoginActivity.context;
    }

}