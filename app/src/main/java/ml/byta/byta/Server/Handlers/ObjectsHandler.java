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

        if (response.isOk() && response.getObjects().size() > 0) {

            List<Object> objects = new ArrayList<>();

            for (int i = 0; i < response.getObjects().size(); i++) {

                String description = response.getObjects().get(i).getDescripcion();
                String location = response.getObjects().get(i).getUbicacion();
                int ownerId = response.getObjects().get(i).getId_usuario();
                int serverId = response.getObjects().get(i).getId();
                long timestamp = (long) response.getObjects().get(i).getFecha_subido();

                Log.d("Main", "" + timestamp);

                Object object = new Object(description, false, location, timestamp, ownerId, serverId);

                objects.add(object);
            }

            // Se almacenan los objetos recibidos en la base de datos local.
            Database.db.objectDao().insertObjects(objects);

            // Se abre la activity "UsuarioRegistrado" y se cierra la activity anterior.
            Intent intent = new Intent(splashActivity, UsuarioRegistrado.class);
            splashActivity.startActivity(intent);
            splashActivity.finish();

            // LO SIGUIENTE ES PARA VER SI LO ALMACENADO EN LA BD ES CORRECTO

            Log.d("Main", "-------------------------------------------------------------------");
            Log.d("Main", "" + response.getObjects().size() + " objetos insertados");
            Log.d("Main", "-------------------------------------------------------------------");

            List<Object> objectsFromDB;
            objectsFromDB = Database.db.objectDao().getAllObjects();

            for (int i = 0; i < objectsFromDB.size(); i++) {
                Log.d("Main", "-------------------------------------------------------------------");
                Log.d("Main", "Description --> " + objectsFromDB.get(i).getDescription());
                Log.d("Main", "Viewed --> " + objectsFromDB.get(i).isViewed());
                Log.d("Main", "Location --> " + objectsFromDB.get(i).getLocation());
                Log.d("Main", "Timestamp --> " + objectsFromDB.get(i).getTimestamp());
                Log.d("Main", "-------------------------------------------------------------------");
            }

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
