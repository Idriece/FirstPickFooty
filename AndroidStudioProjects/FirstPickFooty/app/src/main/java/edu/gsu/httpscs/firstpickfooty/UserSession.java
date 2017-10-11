package edu.gsu.httpscs.firstpickfooty;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;

/**
 * Created by Idriece on 7/12/2017.
 */

public class UserSession {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public UserSession(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("firstpickfooty", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoggedIn(boolean loggedIn, String user) {
        editor.putBoolean("loggedInMode", loggedIn);
        editor.putString("user", user);
        editor.commit();
    }

    public boolean loggedIn() {
        return sharedPreferences.getBoolean("loggedInMode", false);
    }

    public String getUser() {
        return sharedPreferences.getString("user", "");
    }
}
