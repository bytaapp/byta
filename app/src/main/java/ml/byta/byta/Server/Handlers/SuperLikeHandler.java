package ml.byta.byta.Server.Handlers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Object;
import ml.byta.byta.DataBase.SuperlikedObject;
import ml.byta.byta.Server.RequestsToServer;
import ml.byta.byta.Server.Responses.RewindResponse;
import ml.byta.byta.Server.Responses.SuperLikeResponse;

/**
 * Created by maialen on 23/01/18.
 */

public class SuperLikeHandler extends AsyncHttpResponseHandler implements RequestsToServer{

    private Activity activity;
    private  int idObjeto;

    public SuperLikeHandler(Activity activity,int idObjeto) {
        this.activity = activity;
        this.idObjeto=idObjeto;

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {




        Gson gson = new Gson();

        final SharedPreferences settings = activity.getSharedPreferences("Config", 0);
       final SharedPreferences.Editor editor = settings.edit();

        Log.d("etiqueta", new String(responseBody));

        SuperLikeResponse response = gson.fromJson(new String(responseBody), SuperLikeResponse.class);

        if (response.isOk()) {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                            Object object = Database.db.objectDao().getByServerId(idObjeto);
                            SuperlikedObject slo = new SuperlikedObject(object.getDescription(),object.isViewed(), object.getLocation(), object.getTimestamp(), object.getOwnerId(), object.getServerId());
                            Database.db.superlikedObjectDao().insert(slo);
                            //Database.db.objectDao().update(object);
                             getChatsAndMessages();
                        }


                });
            };

        Toast.makeText(activity, "SUPERLIKE! Chat creado correctamete",Toast.LENGTH_SHORT).show();

        }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        Log.d("etiqueta", String.valueOf(error));
    }

    @Override
    public void getChatsAndMessages() {

        SharedPreferences settings = activity.getSharedPreferences("Config", 0);
        // Se hace una petición asíncrona para obtener la lista de chats.
        SyncHttpClient client = new SyncHttpClient();

        // Se hace la petición al servidor. Los mensajes se piden en el handler de los chats.
        client.get(
                activity,
                "https://byta.ml/apiV2/BytaChat/public/index.php/api/chats/" + settings.getInt("userID", 0),
                new ChatsHandler(activity)
        );
    }

    @Override
    public void getObjectsLogged() {

    }
}
