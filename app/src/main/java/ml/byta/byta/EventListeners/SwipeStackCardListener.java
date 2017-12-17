package ml.byta.byta.EventListeners;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.gson.Gson;

import java.util.ArrayList;

import link.fls.swipestack.SwipeStack;
import ml.byta.byta.Activities.AvisoIniciarSesion;
import ml.byta.byta.Activities.MainActivity;
import ml.byta.byta.Objects.Producto;
import ml.byta.byta.R;
import ml.byta.byta.REST.ClasePeticionRest;

public class SwipeStackCardListener implements SwipeStack.SwipeStackListener{

    private ArrayList<Producto> productos;
    Activity activity;
    int idUsuario;
    SharedPreferences settings;

    final String tag = "swipe";

    public SwipeStackCardListener(Activity activity, ArrayList<Producto> productos) {
        this.activity = activity;
        this.productos = productos;
        this.settings = activity.getSharedPreferences("Config", 0);
        this.idUsuario = settings.getInt("id", 0);
        activity.findViewById(R.id.LayoutBotonesYDescripcion).setVisibility(View.VISIBLE);
    }


    @Override
    public void onViewSwipedToLeft(int position) {
        int idObjeto = productos.get(position).getId();

        TextView description = activity.findViewById(R.id.DescripcionCarta);

        if (this.idUsuario != 0) {
            new ClasePeticionRest.GuardarSwipe(activity, this.idUsuario, idObjeto, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            if (productos.size() > position + 1){
                description.setText(productos.get(position + 1).getDescription());
            }else{
                description.setText("");
            }
        }else{
            //Log.d("entro", String.valueOf(position));
            if(position >= 4) {
                Intent intent = new Intent(activity, AvisoIniciarSesion.class);
                activity.startActivity(intent);
                activity.finish();
            }else {
                new ClasePeticionRest.CogerObjetoAleatorioSwipe(activity).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                if (productos.size() > position + 1){
                    description.setText(productos.get(position + 1).getDescription());
                }else{
                    description.setText("");
                }
            }
        }

    }

    @Override
    public void onViewSwipedToRight(int position) {
        int idObjeto = productos.get(position).getId();

        TextView description = activity.findViewById(R.id.DescripcionCarta);

        if (this.idUsuario != 0) {
            new ClasePeticionRest.GuardarSwipe(activity, this.idUsuario, idObjeto, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            if (productos.size() > position + 1){
                description.setText(productos.get(position + 1).getDescription());
            }else{
                description.setText("");
            }
        }else{
            ClasePeticionRest.mostrarCustomToast(activity);
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }

    }

    @Override
    public void onStackEmpty() {
        stackVacio();
    }

    public void stackVacio(){

        activity.findViewById(R.id.imagen_swappie).setVisibility(View.GONE);
        activity.findViewById(R.id.no_imagenes).setVisibility(View.GONE);
        activity.findViewById(R.id.gif).setVisibility(View.GONE);

        activity.findViewById(R.id.LayoutStackVacio).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.pila_cartas).setVisibility(View.GONE);
        activity.findViewById(R.id.LayoutBotonesYDescripcion).setVisibility(View.GONE);
    }

}
