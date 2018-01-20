package ml.byta.byta.Server.Handlers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.Activities.UsuarioRegistrado;
import ml.byta.byta.DataBase.AppDatabase;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Object;
import ml.byta.byta.Server.Responses.ObjectsResponse;

public class ObjectsHandler extends AsyncHttpResponseHandler {

    private Activity splashActivity;

    public ObjectsHandler(Activity splashActivity) {
        this.splashActivity = splashActivity;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        // Respuesta del servidor.
        ObjectsResponse response = gson.fromJson(new String(responseBody), ObjectsResponse.class);

        Log.d("Main", "Respuesta del servidor --> " + gson.toJson(response));

        if (response.isOk() && response.getObjects().size() > 0) {

            List<Object> objects = new ArrayList<>();

            for (int i = 0; i < response.getObjects().size(); i++) {

                String description = response.getObjects().get(i).getDescripcion();
                String location = response.getObjects().get(i).getUbicacion();
                int ownerId = response.getObjects().get(i).getId_usuario();
                int serverId = response.getObjects().get(i).getId();
                long timestamp = (long) response.getObjects().get(i).getFecha_subido();

                Object object = new Object(description, false, location, timestamp, ownerId, serverId);

                objects.add(object);
            }

            // Se almacenan los objetos recibidos en la base de datos local.
            Database.db.objectDao().insertObjects(objects);

            // Se abre la activity "UsuarioRegistrado" y se cierra la activity anterior.
            Intent intent = new Intent(splashActivity, UsuarioRegistrado.class);
            splashActivity.startActivity(intent);
            splashActivity.finish();

        } else {
            // TODO: ¿Qué hacer aquí? La respuesta no es correcta o no se han enviado objetos.
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
