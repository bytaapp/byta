package ml.byta.byta.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.SyncHttpClient;

import java.util.ArrayList;

import ml.byta.byta.Objects.Producto;
import ml.byta.byta.R;
import ml.byta.byta.REST.ClasePeticionRest;
import ml.byta.byta.Server.Handlers.MessagesHandler;
import ml.byta.byta.Server.Handlers.MyObjectsHandler;


public class ListadoObjetos extends AppCompatActivity {

    ListView lista_mis_objetos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_objetos);
        //productos.clear();

        Log.d("productos","Entro a ListadoObjetos.java");
        lista_mis_objetos = (ListView) findViewById(R.id.ListViewMisObjetos);

        SharedPreferences settings = getSharedPreferences("Config", 0);
        //new ClasePeticionRest.CogerInfoObjetos(this, settings.getInt("id", 0)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        SyncHttpClient client = new SyncHttpClient();
        // Pedimos todas sus fotos y las dibujamos
        client.get(
                this,
                "https://byta.ml/apiV2/pedir_objetos.php?modo=fotos_subidas&sessionID=" + settings.getString("sessionID", ""),
                new MyObjectsHandler(this, lista_mis_objetos)
        );

    }
}
