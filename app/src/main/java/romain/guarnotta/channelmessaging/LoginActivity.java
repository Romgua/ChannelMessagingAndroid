package romain.guarnotta.channelmessaging;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

    @Override
    public void onError(String error) {

    }

    @Override
    public void onCompleted(String response) {
        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
    }

}