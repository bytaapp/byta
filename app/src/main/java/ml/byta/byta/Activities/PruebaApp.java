package ml.byta.byta.Activities;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by maialen on 11/10/17.
 */

public class PruebaApp extends Application{

    @Override
    public void onCreate(){

        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

    }
}
