package ml.byta.byta.Server.Handlers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.Activities.UsuarioNoRegistrado;
import ml.byta.byta.Activities.UsuarioRegistrado;
import ml.byta.byta.DataBase.AppDatabase;
import ml.byta.byta.Server.RequestsToServer;
import ml.byta.byta.Server.Responses.LoginResponse;

public class LoginHandler extends AsyncHttpResponseHandler implements RequestsToServer {

    private Activity activity;
    private SharedPreferences settings;
    private String email = "";
    private String password = "";
    private String method = "";
    private AppDatabase db;

    public LoginHandler(Activity activity, SharedPreferences settings, AppDatabase db) {
        this.activity = activity;
        this.settings = settings;
        this.db = db;
    }

    public LoginHandler(Activity activity, String email, String password, String method, SharedPreferences settings, AppDatabase db) {
        this.activity = activity;
        this.email = email;
        this.password = password;
        this.method = method;
        this.settings = settings;
        this.db = db;
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

        Log.d("Main", gson.toJson(response).toString());

        Intent intent;

        if (response.isOk()) {  // Login con éxito.

            // Se cargan los objetos.
            //getObjects();

            // Se cargan los chats y mensajes.
            //getChatsAndMessages();

            // Se carga la activity "UsuarioRegistrado".
            intent = new Intent(activity, UsuarioRegistrado.class);

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

        } else {    // El login no ha tenido éxito.

            // Se cargan los objetos.
            //getObjects();

            // Se carga la activity "UsuarioNoRegistrado".
            intent = new Intent(activity, UsuarioNoRegistrado.class);
            Log.d("Main", "Error en la petición: " + response.getError());
        }

        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        // TODO: TRATAR EL FALLO, ¿CÓMO?
    }

    @Override
    public void getChatsAndMessages() {
        // Se hace una petición asíncrona para obtener la lista de chats.
        AsyncHttpClient client = new AsyncHttpClient();

        // Se hace la petición al servidor. Los mensajes se piden en el handler de los chats.
        client.get(
                activity,
                "https://byta.ml/api/SwappieChat/public/index.php/api/chats/" + settings.getInt("id", 0) + "",
                new ChatsHandler(activity, db)
        );
    }

    @Override
    public void getObjects() {
        // Se hace una petición asíncrona para obtener la lista de chats.
        AsyncHttpClient client = new AsyncHttpClient();

        // TODO: terminar de implementar esta petición.

        // Se hace la petición al servidor.
        client.get(
                activity,
                "https://byta.ml/apiV2/obtener_objetos.php?modo=aleatorio",
                new ObjectsHandler(db)
        );
    }
}
