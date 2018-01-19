package ml.byta.byta.Server.Handlers;

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
import ml.byta.byta.DataBase.AppDatabase;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Object;
import ml.byta.byta.Server.Responses.ObjectsResponse;

public class ObjectsHandler extends AsyncHttpResponseHandler {

    private AppDatabase db;

    public ObjectsHandler(AppDatabase db) {
        this.db = db;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Respuesta del servidor " + gson.toJson(new String(responseBody)));
        Log.d("Main", "-------------------------------------------------------------------");

        // Respuesta del servidor.
        ObjectsResponse response = gson.fromJson(new String(responseBody), ObjectsResponse.class);

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "El servidor ha enviado " + response.getObjects().size() + " objetos");
        Log.d("Main", "-------------------------------------------------------------------");

        if (response.isOk() && response.getObjects().size() > 0) {
            List<Object> objects = new ArrayList<>();

            for (int i = 0; i < response.getObjects().size(); i++) {

                String description = response.getObjects().get(i).getDescripcion();
                String location = response.getObjects().get(i).getUbicacion();
                int ownerId = response.getObjects().get(i).getId_usuario();
                int serverId = response.getObjects().get(i).getId();
                long timestamp = (long) response.getObjects().get(i).getFecha_subido();

                Log.d("Main", "" + timestamp);

                /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = null;
                try {
                    date = simpleDateFormat.parse(response.getObjects().get(i).getFecha_subido());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Timestamp timestamp = new Timestamp(date.getTime());*/

                Object object = new Object(description, false, location, timestamp, ownerId, serverId);

                objects.add(object);
            }

            // Se almacenan los objetos recibidos en la base de datos local.
            Database.db.objectDao().insertObjects(objects);

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
                Log.d("Main", "-------------------------------------------------------------------");
            }

        } else {

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

        Gson gson = new Gson();

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Respuesta del servidor " + gson.toJson(new String(responseBody)));
        Log.d("Main", "-------------------------------------------------------------------");

        // Respuesta del servidor.
        ObjectsResponse response = gson.fromJson(new String(responseBody), ObjectsResponse.class);

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "El servidor ha enviado " + response.getObjects().size() + " objetos");
        Log.d("Main", "Descripción del error " + response.getError());
        Log.d("Main", "-------------------------------------------------------------------");
    }

}
