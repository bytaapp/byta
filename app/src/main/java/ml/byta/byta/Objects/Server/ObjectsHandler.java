package ml.byta.byta.Objects.Server;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.DataBase.AppDatabase;
import ml.byta.byta.DataBase.Object;

public class ObjectsHandler extends AsyncHttpResponseHandler {

    private AppDatabase db;

    public ObjectsHandler(AppDatabase db) {
        this.db = db;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        // Respuesta del servidor.
        ObjectsResponse response = gson.fromJson(new String(responseBody), ObjectsResponse.class);

        if (response.getObjects().size() > 0) {
            List<Object> objects = new ArrayList<>();

            for (int i = 0; i < response.getObjects().size(); i++) {
                objects.add(response.getObjects().get(i));
            }

            // Se almacenan los objetos recibidos en la base de datos local.
            db.objectDao().insertObjects(objects);

        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        // TRATAR EL FALLO, ¿CÓMO?
    }

}
