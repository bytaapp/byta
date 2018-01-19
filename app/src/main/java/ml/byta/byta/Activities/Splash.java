package ml.byta.byta.Activities;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.loopj.android.http.AsyncHttpClient;

import java.util.Timer;
import java.util.TimerTask;

import ml.byta.byta.DataBase.AppDatabase;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.Server.Handlers.ChatsHandler;
import ml.byta.byta.Server.Handlers.LoginHandler;
import ml.byta.byta.Server.Handlers.ObjectsHandler;
import ml.byta.byta.Server.RequestsToServer;
import ml.byta.byta.R;


public class Splash extends Activity {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 2400;

    private Database database;
    public static AppDatabase db;

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

        this.database = new Database(this);
        db = Database.db;
        //db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "local-database").build();

        // Se hace login.
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

    private void login() {
        SharedPreferences settings = getSharedPreferences("Config", 0);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "SessionID --> " + settings.getString("sessionID", ""));
        Log.d("Main", "Email --> " + settings.getString("email", ""));
        Log.d("Main", "Password --> " + settings.getString("password", ""));
        Log.d("Main", "Name --> " + settings.getString("name", ""));
        Log.d("Main", "Surname --> " + settings.getString("surname", ""));
        Log.d("Main", "Location --> " + settings.getString("location", ""));
        Log.d("Main", "-------------------------------------------------------------------");

        LoginHandler loginHandler;

        if (settings.getString("sessionID", "").equals("")) {
            // No hay una sesión iniciada.
            loginHandler = new LoginHandler(this, settings, db);
        } else {
            // Hay una sesión iniciada.
            loginHandler = new LoginHandler(this, settings.getString("email", ""), settings.getString("password", ""),
                    settings.getString("method", ""), settings, db);
        }

        // Se hace la petición al servidor.
        client.addHeader("X-AUTH-TOKEN", settings.getString("sessionID", ""));
        client.get(
                this,
                "https://byta.ml/apiV2/login.php?email=" + settings.getString("email", "")
                        + "&password=" + settings.getString("password", "") + "&nombre=" +
                        settings.getString("name", "") + "&apellidos=" + settings.getString("surname", "")
                        + "&ubicacion=" + settings.getString("location", ""),
                loginHandler
        );

    }

}