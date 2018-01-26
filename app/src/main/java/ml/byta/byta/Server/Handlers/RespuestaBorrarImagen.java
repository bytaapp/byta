package ml.byta.byta.Server.Handlers;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.Server.Responses.ObjectsResponse;
import ml.byta.byta.Server.Responses.RewindResponse;

/**
 * Created by iker on 1/26/18.
 */

public class RespuestaBorrarImagen extends AsyncHttpResponseHandler{

    Activity activity;
    int id_producto;

    public RespuestaBorrarImagen(Activity activity, int id_producto) {
        this.activity = activity;
        this.id_producto = id_producto;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        Gson gson = new Gson();

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Respuesta del servidor al intentar borrar la imagen--> " + new String(responseBody));
        Log.d("Main", "-------------------------------------------------------------------");

        RewindResponse response = gson.fromJson(new String(responseBody), RewindResponse.class);

        if (response.isOk()) {
            // Se ha borrado correctamente
            Log.d("Main","Objeto borrado con éxito");
            activity.recreate();
        }
        else
        {
            Log.d("Main","Error al borrar el objeto");
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

    }
}
