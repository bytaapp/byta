package ml.byta.byta.Server.Handlers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.DataBase.Chat;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Object;
import ml.byta.byta.Server.Responses.ChatsResponse;
import ml.byta.byta.Server.Responses.ObjectsResponse;

/**
 * Created by iker on 1/25/18.
 */

public class MeGustaHandler extends AsyncHttpResponseHandler {

    private Activity activity;

    public MeGustaHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        /* LA RESPUESTA DEL SERVIDOR TAMBIÉN LLEVA OBJETOS POR SI HAY QUE UTILIZARLOS.
         * OBJETOS CUYO MATCH HA GENERADO EL CHAT.
         */

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Respuesta del servidor al pedir los meGusta --> " + new String(responseBody));
        Log.d("Main", "-------------------------------------------------------------------");

        // Respuesta del servidor.
        ObjectsResponse response = gson.fromJson(new String(responseBody), ObjectsResponse.class);

        if (response.isOk() && response.getObjects().size() > 0) {

            List<Object> objects = new ArrayList<>();

            for (int i = 0; i < response.getObjects().size(); i++) {

                String description = response.getObjects().get(i).getDescripcion();
                String location = response.getObjects().get(i).getUbicacion();
                int ownerId = response.getObjects().get(i).getId_usuario();
                int serverId = response.getObjects().get(i).getId();
                long timestamp = (long) response.getObjects().get(i).getFecha_subido();

                Object object = new Object(description, true, location, timestamp, ownerId, serverId);

                objects.add(object);
            }

            Log.d("Main", "-------------------------------------------------------------------");
            Log.d("Main", "Se han recibido " + response.getObjects().size() + " objetos que me gustan del servidor");
            Log.d("Main", "-------------------------------------------------------------------");

            // Se almacenan los objetos recibidos en la base de datos local.
            Database.db.objectDao().insert_or_update(objects);

        } else {
            // "ok" es false o no se han enviado chats.
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        // TODO: TRATAR EL FALLO, ¿CÓMO?
        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "ERROR --> Ha entrado en onFailure");
        Log.d("Main", "Código de error --> " + statusCode);
        Log.d("Main", "Throwable error --> " + error);
        Log.d("Main", "-------------------------------------------------------------------");
    }
}
