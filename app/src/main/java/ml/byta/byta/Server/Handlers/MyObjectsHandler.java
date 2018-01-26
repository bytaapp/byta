package ml.byta.byta.Server.Handlers;

import android.app.Activity;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.Adapters.AdapterListadoObjetos;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Message;
import ml.byta.byta.Objects.Object;
import ml.byta.byta.Objects.Producto;
import ml.byta.byta.Server.ModelsFromServer.ObjectFromServer;
import ml.byta.byta.Server.Responses.MessagesResponse;
import ml.byta.byta.Server.Responses.ObjectsResponse;

/**
 * Created by iker on 1/26/18.
 */

public class MyObjectsHandler extends AsyncHttpResponseHandler{

    Activity activity;
    ListView lista;

    public MyObjectsHandler(Activity activity, ListView lista)
    {
        this.activity = activity;
        this.lista = lista;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        ObjectsResponse response = gson.fromJson(new String(responseBody), ObjectsResponse.class);

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Respuesta del servidor al pedir los objetos que he subido--> " + new String(responseBody));
        Log.d("Main", "-------------------------------------------------------------------");

        if (response.isOk()) {

            ArrayList<ObjectFromServer> objetos_subidos = new ArrayList<ObjectFromServer>();
            objetos_subidos = (ArrayList) response.getObjects();
            AdapterListadoObjetos listado_objetos = new AdapterListadoObjetos(activity, objetos_subidos);

            lista.setAdapter(listado_objetos);


        } else {
            // "ok" es false o no se han enviado mensajes.
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "ERROR --> Ha entrado en onFailure");
        Log.d("Main", "Código de error --> " + statusCode);
        Log.d("Main", "Throwable error --> " + error);
        Log.d("Main", "-------------------------------------------------------------------");
    }
}
