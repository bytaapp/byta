package ml.byta.byta.Server.Handlers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import link.fls.swipestack.SwipeStack;
import ml.byta.byta.Activities.UsuarioNoRegistrado;
import ml.byta.byta.Activities.UsuarioRegistrado;
import ml.byta.byta.Adapters.AdapterProductos;
import ml.byta.byta.DataBase.AppDatabase;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Object;
import ml.byta.byta.EventListeners.SwipeStackCardListener;
import ml.byta.byta.Objects.Producto;
import ml.byta.byta.Server.RequestsToServer;
import ml.byta.byta.Server.Responses.ObjectsResponse;

public class ObjectsHandler extends AsyncHttpResponseHandler implements RequestsToServer{

    private Activity activity;
    private boolean registered;
    private SwipeStack swipeStack;

    public ObjectsHandler(Activity activity, boolean registered) {
        this.activity = activity;
        this.registered = registered;
    }

    public ObjectsHandler(Activity activity, boolean registered, SwipeStack swipeStack) {
        this.activity = activity;
        this.registered = registered;
        this.swipeStack = swipeStack;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        // Respuesta del servidor.
        ObjectsResponse response = gson.fromJson(new String(responseBody), ObjectsResponse.class);

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Respuesta del servidor al pedir objetos --> " + gson.toJson(response));
        Log.d("Main", "-------------------------------------------------------------------");

        if (response.isOk()) {

            if (isRegistered()) {   // Usuario registrado.

                if (response.getObjects().size() > 0) {
                    Log.d("Main", "-------------------------------------------------------------------");
                    Log.d("Main", "La respuesta es correcta, el usuario está registrado y ha recibido objetos");
                    Log.d("Main", "-------------------------------------------------------------------");

                    List<Object> objects = new ArrayList<>();

                    for (int i = 0; i < response.getObjects().size(); i++) {

                        String description = response.getObjects().get(i).getDescripcion();
                        String location = response.getObjects().get(i).getUbicacion();
                        int ownerId = response.getObjects().get(i).getId_usuario();
                        int serverId = response.getObjects().get(i).getId();
                        long timestamp = (long) response.getObjects().get(i).getFecha_subido();

                        Object object = new Object(description, false, location, timestamp, ownerId, serverId);

                        objects.add(object);
                    }

                    Log.d("Main", "-------------------------------------------------------------------");
                    Log.d("Main", "Se han recibido " + response.getObjects().size() + " objetos del servidor");
                    Log.d("Main", "-------------------------------------------------------------------");

                    // Se almacenan los objetos recibidos en la base de datos local.
                    Database.db.objectDao().insertObjects(objects);


                } else {
                    Log.d("Main", "-------------------------------------------------------------------");
                    Log.d("Main", "La respuesta es correcta, el usuario está registrado pero no ha recibido objetos");
                    Log.d("Main", "-------------------------------------------------------------------");
                }

                // Actualizamos los nuevos likes
                getLikedObject();


                // Se abre la activity "UsuarioRegistrado" y se cierra la activity anterior.
                /*Intent intent = new Intent(activity, UsuarioRegistrado.class);
                activity.startActivity(intent);
                activity.finish();*/

            } else {
                Log.d("Main", "-------------------------------------------------------------------");
                Log.d("Main", "La respuesta es correcta pero el usuario no está registrado");
                Log.d("Main", "-------------------------------------------------------------------");

                List<Producto> productos = new ArrayList<>();

                Producto producto;

                for (int i = 0; i < response.getObjects().size(); i++) {
                    producto = new Producto(
                            response.getObjects().get(i).getDescripcion(),
                            "",
                            response.getObjects().get(i).getId());
                    productos.add(producto);
                }

                AdapterProductos adapterProductos = new AdapterProductos(activity, productos);
                swipeStack.setAdapter(adapterProductos);
                swipeStack.setListener(new SwipeStackCardListener(activity, productos));
                adapterProductos.notifyDataSetChanged();
            }


        } else {
            // TODO: ¿Qué hacer aquí? La respuesta no es correcta.
            Log.d("Main", "-------------------------------------------------------------------");
            Log.d("Main", "La respuesta no es correcta");
            Log.d("Main", "-------------------------------------------------------------------");
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        // TODO: TRATAR EL FALLO, ¿CÓMO?
        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "ERROR --> Ha entrado en onFailure al pedir objetos");
        Log.d("Main", "Código de error --> " + statusCode);
        Log.d("Main", "Throwable error --> " + error);
        Log.d("Main", "-------------------------------------------------------------------");
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    @Override
    public void getChats() {

    }

    @Override
    public void getLikedObject() {
        // Se hace una petición asíncrona para obtener los objetos likeados
        SyncHttpClient client = new SyncHttpClient();
        SharedPreferences settings = activity.getSharedPreferences("Config", 0);

        long timestamp;
        // Se comprueba si el objeto extraido de la BD no es null, es decir, si hay objetos almacenados.
        Object lastObjectInTimeLiked = Database.db.objectDao().getLastObjectInTimeLiked();
        if (lastObjectInTimeLiked == null) {
            Log.d("Main", "-------------------------------------------------------------------");
            Log.d("Main", "lastObjectInTimeLiked es NULL");
            Log.d("Main", "-------------------------------------------------------------------");

            timestamp = 0;
        } else {

            Log.d("Main", "-------------------------------------------------------------------");
            Log.d("Main", "Descripción objeto más reciente --> " + lastObjectInTimeLiked.getDescription());
            Log.d("Main", "-------------------------------------------------------------------");
            timestamp = lastObjectInTimeLiked.getTimestamp();
        }
        Log.e("Debug", timestamp+"");

        // Se hace la petición al servidor. Los mensajes se piden en el handler de los chats.
        String url = "https://byta.ml/apiV2/pedir_objetos.php?modo=meGusta&timestamp="+timestamp+"&sessionID=" +
                settings.getString("sessionID", "");
        client.get(
                activity,
                url,
                new MeGustaHandler(activity)
        );
    }

    @Override
    public void getObjectsLogged() {

    }

    @Override
    public void getMessages() {

    }
}
