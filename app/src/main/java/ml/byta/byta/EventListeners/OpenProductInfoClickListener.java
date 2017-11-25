package ml.byta.byta.EventListeners;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import ml.byta.byta.Objects.Producto;

public class OpenProductInfoClickListener implements View.OnClickListener {

    private Activity activity;
    private Producto producto;

    public OpenProductInfoClickListener(Activity activity, Producto producto) {
        this.activity = activity;
        this.producto = producto;
    }

    @Override
    public void onClick(View v) {
        String description = "Descripción: " + producto.getDescription();
        String location = "Localización: " + producto.getLocation();
        Log.d("Main", description + "   " + location);
    }
}
