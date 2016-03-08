package romain.guarnotta.channelmessaging.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import romain.guarnotta.channelmessaging.R;

/**
 * Created by romain on 08/02/16.
 */
public class MessageActivity extends GPSActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_message);
    }
}
