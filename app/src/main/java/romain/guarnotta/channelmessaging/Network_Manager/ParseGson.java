package romain.guarnotta.channelmessaging.Network_Manager;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

import java.net.ConnectException;

import romain.guarnotta.channelmessaging.Model.ConnectResponse;

/**
 * Created by romain on 08/02/16.
 */
public class ParseGson<T> {

    public static String PREFS_NAME = "MyPrefsFile";

    public static <T> T parseGson(Class<T> myClassObject, String response) {
        Gson gson = new Gson();

        return gson.fromJson(response, myClassObject);
    }

    public static void stockAccessTokenToPrefsFile(Context myContext, String response) throws ConnectException {
        ConnectResponse connectResponse = parseGson(ConnectResponse.class, response);

        if (!connectResponse.getCode().equals("200")) {
            throw new ConnectException();
        }

        stockPreferences(myContext, "accesstoken", connectResponse.getAccesstoken());
    }

    public static void stockPreferences(Context myContext, String key, String value) {
        SharedPreferences settings = myContext.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);

        // Commit the edits
        editor.apply();
    }

    public static void removePreferencesByKey(Context myContext, String key) {
        SharedPreferences settings = myContext.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);

        // Commit the edits
        editor.apply();
    }

    public static String getInfoInPrefsFileByKey(SharedPreferences settings, String key) {
        return settings.getString(key, "error");
    }

}
