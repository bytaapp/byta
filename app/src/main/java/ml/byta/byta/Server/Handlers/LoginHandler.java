package ml.byta.byta.Server.Handlers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.Activities.UsuarioNoRegistrado;
import ml.byta.byta.Activities.UsuarioRegistrado;
import ml.byta.byta.DataBase.AppDatabase;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Object;
import ml.byta.byta.Server.RequestsToServer;
import ml.byta.byta.Server.Responses.LoginResponse;

public class LoginHandler extends AsyncHttpResponseHandler implements RequestsToServer {

    private Activity activity;
    private SharedPreferences settings;
    private String email = "";
    private String password = "";
    private String method = "";

    public LoginHandler(Activity activity, SharedPreferences settings) {
        this.activity = activity;
        this.settings = settings;
    }

    public LoginHandler(Activity activity, String email, String password, String method, SharedPreferences settings) {
        this.activity = activity;
        this.email = email;
        this.password = password;
        this.method = method;
        this.settings = settings;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        LoginResponse response = gson.fromJson(new String(responseBody), LoginResponse.class);

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Respuesta del servidor --> " + gson.toJson(response));
        Log.d("Main", "-------------------------------------------------------------------");

        if (response.isOk()) {  // Login con éxito.

            // Se almacena la info en SharedPreferences.
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("sessionID", response.getSessionID());
            editor.putInt("userID", response.getId_usuario());
            editor.putString("name", response.getNombre());
            editor.putString("surname", response.getApellidos());
            editor.putString("email", email);
            editor.putString("password", password);
            editor.putString("method", method);
            editor.commit();

            /* Se cargan los objetos, chats y mensajes. Como primero hay que acceder a la BD local,
             * se crean hilos independientes. La petición al servidor es síncrona porque ya estamos
             * en un hilo independiente del hilo principal.
             */

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    getObjectsLogged();
                }
            });

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    getChatsAndMessages();
                }
            });

        } else {    // El login no ha tenido éxito.

            // Se cargan los objetos.
            //getObjects();

            // Se carga la activity "UsuarioNoRegistrado".
            Intent intent = new Intent(activity, UsuarioNoRegistrado.class);
            activity.startActivity(intent);
            //activity.finish();
            Log.d("Main", "Error en la petición: " + response.getError());
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

    @Override
    public void getChatsAndMessages() {
        // Se hace una petición asíncrona para obtener la lista de chats.
        SyncHttpClient client = new SyncHttpClient();

        // Se hace la petición al servidor. Los mensajes se piden en el handler de los chats.
        client.get(
                activity,
                "https://byta.ml/apiV2/BytaChat/public/index.php/api/chats/" + settings.getInt("userID", 0),
                new ChatsHandler(activity)
        );
    }

    @Override
    public void getObjectsLogged() {
        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Se han pedido objetos");
        Log.d("Main", "-------------------------------------------------------------------");

        // Se selecciona el último objeto almacenado por su timestamp.
        Object lastObjectInTime = Database.db.objectDao().getLastObjectInTime();

        // Se hace una petición síncrona porque ya se hace en un hilo independiente.
        SyncHttpClient client = new SyncHttpClient();

        String url = "";

        // Se comprueba si el objeto extraido de la BD no es null, es decir, si hay objetos almacenados.
        if (lastObjectInTime == null) {
            // Timestamp = 0.
            url = "https://byta.ml/apiV2/pedir_objetos.php?modo=registrado&timestamp=0&sessionID=" +
                    settings.getString("sessionID", "");
        } else {
            url = "https://byta.ml/apiV2/pedir_objetos.php?modo=registrado&timestamp=" + lastObjectInTime.getTimestamp()
                    + "&sessionID=" + settings.getString("sessionID", "");

            Log.d("Main", "-------------------------------------------------------------------");
            Log.d("Main", "Valor de getTimestamp() --> " + lastObjectInTime.getTimestamp());
            Log.d("Main", "-------------------------------------------------------------------");
        }

        // Se hace la petición al servidor.
        client.get(
                activity,
                url,
                new ObjectsHandler(activity)
        );

    }
}
