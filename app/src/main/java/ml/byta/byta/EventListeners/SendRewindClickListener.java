package ml.byta.byta.EventListeners;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;

import ml.byta.byta.Server.Handlers.RewindHandler;

public class SendRewindClickListener implements View.OnClickListener {

    private Activity activity;
    private int num;

    public SendRewindClickListener(Activity activity, int num) {
        this.activity = activity;
        this.num = num;
    }

    @Override
    public void onClick(View view) {

        SharedPreferences settings = activity.getSharedPreferences("Config", 0);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(
                activity,
                "https://byta.ml/apiV2/gestionar_objetos.php?sessionID=" + settings.getString("sessionID", "")
                        + "&modo=rewind&numero=" + num,
                new RewindHandler(activity)
        );

    }

}
