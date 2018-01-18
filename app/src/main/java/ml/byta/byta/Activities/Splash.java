package ml.byta.byta.Activities;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

import com.loopj.android.http.AsyncHttpClient;

import java.util.Timer;
import java.util.TimerTask;

import ml.byta.byta.DataBase.AppDatabase;
import ml.byta.byta.Server.Handlers.ChatsHandler;
import ml.byta.byta.Server.Handlers.LoginHandler;
import ml.byta.byta.Server.Handlers.ObjectsHandler;
import ml.byta.byta.Server.RequestsToServer;
import ml.byta.byta.R;


public class Splash extends Activity implements RequestsToServer {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 2400;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);

        // TODO: comprobar que no peta la base de datos local por lo que se explica a continuación.

        /* PUEDE QUE AQUÍ DE ERROR LA BASE DE DATOS PORQUE SE HACE getApplicationContext() EN UNA
         * ACTIVITY QUE SE CIERRA. COMPROBARLO!!!!
         */
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "local-database").build();

        /*AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                firstLogin();
            }
        });*/


        login();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                /* Tras acabar las imágenes del inicio se carga la actividad "UsuarioRegistrado" o
                 * "UsuarioNoRegistrado", dependiendo de si hay una sesión iniciada o no, y finaliza
                 * la actividad del inicio (la de las imágenes).
                 */
                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);

    }

    @Override
    public void getChatsAndMessages() {
        // Se hace una petición asíncrona para obtener la lista de chats.
        AsyncHttpClient client = new AsyncHttpClient();

        // Se toma el ID del usuario.
        SharedPreferences settings = getSharedPreferences("Config", 0);

        // Se hace la petición al servidor. Los mensajes se piden en el handler de los chats.
        client.get(
                this,
                "https://byta.ml/api/SwappieChat/public/index.php/api/chats/" + settings.getInt("id", 0) + "",
                new ChatsHandler(this, db)
        );
    }

    @Override
    public void getObjects() {
        // Se hace una petición asíncrona para obtener la lista de chats.
        AsyncHttpClient client = new AsyncHttpClient();

        // TODO: terminar de implementar esta petición.

        // Se hace la petición al servidor.
        client.get(
                this,
                "https://byta.ml/apiV2/obtener_objetos.php?modo=aleatorio",
                new ObjectsHandler(db)
        );
    }


    @Override
    public void login() {
        SharedPreferences settings = getSharedPreferences("Config", 0);

        AsyncHttpClient client = new AsyncHttpClient();

        // Se hace la petición al servidor.
        client.addHeader("X-AUTH-TOKEN", settings.getString("sessionID", ""));
        client.get(
                this,
                "https://byta.ml/apiV2/login.php?email=" + settings.getString("email", "")
                        + "&password=" + settings.getString("password", "") + "&nombre=" +
                        settings.getString("name", "") + "&apellidos=" + settings.getString("surname", "")
                        + "&ubicacion=" + settings.getString("location", ""),
                new LoginHandler(this, settings)
        );

    }

}