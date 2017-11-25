package ml.byta.byta.Activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import ml.byta.byta.Objects.Producto;
import ml.byta.byta.R;
import ml.byta.byta.REST.ClasePeticionRest;


public class ListadoObjetos extends AppCompatActivity {

    public static ArrayList<Producto> productos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_objetos);
        productos.clear();

        Log.d("productos","Entro a ListadoObjetos.java");

        SharedPreferences settings = getSharedPreferences("Config", 0);
        new ClasePeticionRest.CogerInfoObjetos(this, settings.getInt("id", 0)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}
