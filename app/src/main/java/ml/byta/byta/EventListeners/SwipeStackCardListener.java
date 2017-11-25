package ml.byta.byta.EventListeners;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import link.fls.swipestack.SwipeStack;
import ml.byta.byta.Activities.MainActivity;
import ml.byta.byta.Objects.Producto;
import ml.byta.byta.R;
import ml.byta.byta.REST.ClasePeticionRest;

public class SwipeStackCardListener implements SwipeStack.SwipeStackListener{

    private ArrayList<Producto> productos;
    Activity activity;
    int idUsuario;
    SharedPreferences settings;

    public SwipeStackCardListener(Activity activity, ArrayList<Producto> productos) {
        this.activity = activity;
        this.productos = productos;
        this.settings = activity.getSharedPreferences("Config", 0);
        this.idUsuario = settings.getInt("id", 0);
    }


    @Override
    public void onViewSwipedToLeft(int position) {
        int idObjeto = productos.get(position).getId();

        TextView description = activity.findViewById(R.id.DescripcionCarta);

        if (this.idUsuario != 0) {
            new ClasePeticionRest.GuardarSwipe(activity, this.idUsuario, idObjeto, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new ClasePeticionRest.CogerObjetoAleatorioSwipe(activity).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        if (productos.size() > position + 1){
            description.setText(productos.get(position + 1).getDescription());
        }else{
            description.setText("");
            activity.findViewById(R.id.pila_cartas).setVisibility(View.GONE);
            ((ImageView)activity.findViewById(R.id.imagen_swappie)).setImageResource(R.drawable.sorry);
            activity.findViewById(R.id.imagen_swappie).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.no_imagenes).setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onViewSwipedToRight(int position) {
        int idObjeto = productos.get(position).getId();

        TextView description = activity.findViewById(R.id.DescripcionCarta);

        if (this.idUsuario != 0) {
            new ClasePeticionRest.GuardarSwipe(activity, this.idUsuario, idObjeto, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            ClasePeticionRest.mostrarCustomToast(activity);
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }

        if (productos.size() > position + 1){
            description.setText(productos.get(position + 1).getDescription());
        }else{
            description.setText("");
            activity.findViewById(R.id.pila_cartas).setVisibility(View.GONE);
            ((ImageView)activity.findViewById(R.id.imagen_swappie)).setImageResource(R.drawable.sorry);
            activity.findViewById(R.id.imagen_swappie).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.no_imagenes).setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onStackEmpty() {
        Log.e("etiqueta", "Stack vacio");
    }

}
