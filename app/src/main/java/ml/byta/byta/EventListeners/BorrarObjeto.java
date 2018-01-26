package ml.byta.byta.EventListeners;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.SyncHttpClient;

import ml.byta.byta.Objects.Producto;
import ml.byta.byta.REST.ClasePeticionRest;
import ml.byta.byta.Server.Handlers.MyObjectsHandler;
import ml.byta.byta.Server.Handlers.RespuestaBorrarImagen;

/**
 * Created by ibai on 11/19/17.
 */

public class BorrarObjeto implements View.OnClickListener{

    Activity activity;
    Producto producto;

    public BorrarObjeto(Activity activity, Producto producto) {
        this.activity = activity;
        this.producto = producto;
    }

    @Override
    public void onClick(View view) {
        // Aqui borramos la imagen del servidor
        Log.d("Main", "Peticion para borrar la imagen "+producto.getId());
        SharedPreferences settings = activity.getSharedPreferences("Config", 0);
        SyncHttpClient client = new SyncHttpClient();
        client.get(
                activity,
                "https://byta.ml/apiV2/gestionar_objetos.php?modo=borrar&sessionID=" + settings.getString("sessionID", "")+"&id_objeto="+producto.getId(),
                new RespuestaBorrarImagen(activity, producto.getId())
        );
    }
}
