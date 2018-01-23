package ml.byta.byta.Server.Handlers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.Activities.Rewind;
import ml.byta.byta.Activities.UsuarioRegistrado;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Object;
import ml.byta.byta.Server.Responses.RewindResponse;

public class RewindHandler extends AsyncHttpResponseHandler{

    private Activity activity;

    public RewindHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        final SharedPreferences settings = activity.getSharedPreferences("Config", 0);

        Log.d("Main", new String(responseBody));

        RewindResponse response = gson.fromJson(new String(responseBody), RewindResponse.class);

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Respuesta del servidor al hacer rewind --> " + gson.toJson(response));
        Log.d("Main", "-------------------------------------------------------------------");

        if (response.isOk()) {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    // Se borra la tabla de objetos.
                    Database.db.objectDao().deleteAllObjects();

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("restartFromRewind", "yes");
                    editor.commit();

                    activity.finish();

                }
            });

        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

    }

}
