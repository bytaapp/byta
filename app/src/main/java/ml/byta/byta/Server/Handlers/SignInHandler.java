package ml.byta.byta.Server.Handlers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.Activities.UsuarioRegistrado;
import ml.byta.byta.Server.Responses.SignInResponse;

public class SignInHandler extends AsyncHttpResponseHandler {

    private Activity activity;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String location;

    public SignInHandler(Activity activity, String name, String surname, String email, String password, String location) {
        this.activity = activity;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.location = location;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        Log.d("Main", gson.toJson(new String(responseBody)));

        SignInResponse response = gson.fromJson(new String(responseBody), SignInResponse.class);

        if (response.isOk()) {

            // Se almacena la info en SharedPreferences.
            SharedPreferences settings = activity.getSharedPreferences("Config", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("sessionID", response.getSessionID());
            editor.putInt("userID", response.getId_usuario());
            editor.putString("name", name);
            editor.putString("surname", surname);
            editor.putString("email", email);
            editor.putString("password", password);
            editor.putString("location", location);
            editor.putString("method", "email");
            editor.commit();

            login();
        } else {
            // Se deja al usuario en la pantalla de registro.
            Log.d("Main", "-------------------------------------------------------------------");
            Log.d("Main", "La respuesta no es correcta");
            Log.d("Main", "-------------------------------------------------------------------");
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        // TODO: TRATAR EL FALLO, ¿CÓMO?
        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "ERROR --> Ha entrado en onFailure al registrarse");
        Log.d("Main", "Código de error --> " + statusCode);
        Log.d("Main", "Throwable error --> " + error);
        Log.d("Main", "-------------------------------------------------------------------");
    }

    private void login() {
        SharedPreferences settings = activity.getSharedPreferences("Config", 0);

        AsyncHttpClient client = new AsyncHttpClient();

        LoginHandler loginHandler;

        if (settings.getString("sessionID", "").equals("")) {
            // No hay una sesión iniciada.
            loginHandler = new LoginHandler(activity, settings);
        } else {
            // Hay una sesión iniciada.
            loginHandler = new LoginHandler(activity, settings.getString("email", ""), settings.getString("password", ""),
                    settings.getString("method", ""), settings);
        }

        // Se hace la petición al servidor.
        client.addHeader("X-AUTH-TOKEN", settings.getString("sessionID", ""));
        client.get(
                activity,
                "https://byta.ml/apiV2/login.php?email=" + settings.getString("email", "")
                        + "&password=" + settings.getString("password", "") + "&nombre=" +
                        settings.getString("name", "") + "&apellidos=" + settings.getString("surname", "")
                        + "&ubicacion=" + settings.getString("location", ""),
                loginHandler
        );

    }

}
