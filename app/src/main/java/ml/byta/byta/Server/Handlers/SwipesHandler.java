package ml.byta.byta.Server.Handlers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Object;
import ml.byta.byta.Server.Responses.SwipesResponse;

public class SwipesHandler extends AsyncHttpResponseHandler {

    private boolean decission;
    private int id;

    public SwipesHandler(boolean decission, int id) {
        this.decission = decission;
        this.id = id;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        SwipesResponse response = gson.fromJson(new String(responseBody), SwipesResponse.class);

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Respuesta del servidor al swipe --> " + gson.toJson(response));
        Log.d("Main", "-------------------------------------------------------------------");

        if (response.isOk()) {  // Respuesta del servidor correcta.

            if (decission == false) {
                // Se elimina el objeto de la base de datos local.
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Database.db.objectDao().deleteByServerId(id);
                    }
                });
            } else if (decission == true) {
                // Se actualiza el objeto en la base de datos local con "viewed" = true.
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Object object = Database.db.objectDao().getByServerId(id);
                        object.setViewed(true);
                        Database.db.objectDao().update(object);
                    }
                });
            }

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
