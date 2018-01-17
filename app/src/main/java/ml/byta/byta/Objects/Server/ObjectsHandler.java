package ml.byta.byta.Objects.Server;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.DataBase.AppDatabase;

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

        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

    }

}
