package ml.byta.byta.EventListeners;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;

import ml.byta.byta.Objects.Producto;
import ml.byta.byta.REST.ClasePeticionRest;

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
        new ClasePeticionRest.BorrarObjeto(activity, producto).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
