package ml.byta.byta.EventListeners;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;

import link.fls.swipestack.SwipeStack;
import ml.byta.byta.Activities.AvisoIniciarSesion;
import ml.byta.byta.Activities.MainActivity;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Object;
import ml.byta.byta.Objects.Producto;
import ml.byta.byta.R;
import ml.byta.byta.REST.ClasePeticionRest;
import ml.byta.byta.Server.Handlers.SuperLikeHandler;
import ml.byta.byta.Server.Handlers.SwipesHandler;

public class SwipeStackCardListener implements SwipeStack.SwipeStackListener{

    private List<Producto> productos;
    Activity activity;
    int idUsuario;
    SharedPreferences settings;

    public SwipeStackCardListener(Activity activity, List<Producto> productos) {
        this.activity = activity;
        this.productos = productos;
        this.settings = activity.getSharedPreferences("Config", 0);
        this.idUsuario = settings.getInt("id", 0);
//        activity.findViewById(R.id.LayoutBotonesYDescripcion).setVisibility(View.VISIBLE);
    }


    @Override
    public void onViewSwipedToLeft(int position) {
        // ID del objeto al que se ha hecho swipe left.
        int id = productos.get(position).getId();

        if (settings.getString("sessionID", "").equals("")) {   // Usuario no registrado.

            // Si no estás registrado, solo puedes jacer 5 swipes left.
            if (position >= 4) {
                Intent intent = new Intent(activity, AvisoIniciarSesion.class);
                activity.startActivity(intent);
                activity.finish();
            }

        } else {    // Usuario registrado.

            // Se notifica al servidor.
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(
                    activity,
                    "https://byta.ml/apiV2/gestionar_objetos.php?modo=swipe&decision=false&id_objeto=" + id + "&sessionID=" + settings.getString("sessionID", ""),
                    new SwipesHandler(false, id)
            );

        }

    }

    @Override
    public void onViewSwipedToRight(int position) {
        // ID del objeto al que se ha hecho swipe left.
        int id = productos.get(position).getId();

        if (settings.getString("sessionID", "").equals("")) {   // Usuario no registrado.

            // Si no estás registrado, no puedes hacer swipe right.
            Intent intent = new Intent(activity, AvisoIniciarSesion.class);
            activity.startActivity(intent);
            activity.finish();

        } else if (!settings.getString("superLike", "").equals("si")){    // Usuario registrado. Es un like, no súper like.
            Log.d("Main", "-------------------------------------------------------------------");
            Log.d("Main", "Has hecho LIKE");
            Log.d("Main", "-------------------------------------------------------------------");

            // Se notifica al servidor.
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(
                    activity,
                    "https://byta.ml/apiV2/gestionar_objetos.php?modo=swipe&decision=true&id_objeto=" + id + "&sessionID=" + settings.getString("sessionID", ""),
                    new SwipesHandler(true, id)
            );

        } else if (settings.getString("superLike", "").equals("si")){   // Usuario registrado. Sí es súper like.
            Log.d("Main", "-------------------------------------------------------------------");
            Log.d("Main", "Has hecho SUPERLIKE");
            Log.d("Main", "-------------------------------------------------------------------");
            // Se notifica al servidor.
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(
                    activity,
                    "https://byta.ml/apiV2/gestionar_objetos.php?sessionID="+settings.getString("sessionID","")+"&modo=superLike&id_objeto="+
                            id, new SuperLikeHandler(activity, id)
            );





        }

    }

    @Override
    public void onStackEmpty() {
        stackVacio();
    }

    public void stackVacio(){

       /* activity.findViewById(R.id.imagen_swappie).setVisibility(View.GONE);
        activity.findViewById(R.id.no_imagenes).setVisibility(View.GONE);
        activity.findViewById(R.id.gif).setVisibility(View.GONE);

        activity.findViewById(R.id.LayoutStackVacio).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.pila_cartas).setVisibility(View.GONE);
        activity.findViewById(R.id.LayoutBotonesYDescripcion).setVisibility(View.GONE); */
    }

}
