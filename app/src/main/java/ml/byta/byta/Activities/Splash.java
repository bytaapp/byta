package ml.byta.byta.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;

import java.util.Timer;
import java.util.TimerTask;

import ml.byta.byta.Objects.Server.ChatsHandler;
import ml.byta.byta.Objects.Server.RequestsToServer;
import ml.byta.byta.R;
import ml.byta.byta.REST.ClasePeticionRest;
import ml.byta.byta.Tools.Connectivity;
import pl.droidsonroids.gif.GifImageView;


public class Splash extends Activity implements RequestsToServer {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 2400;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                if (sessionExists()) {
                    /* Hay una sesión iniciada. Se piden los objetos, chats y mensajes.
                     * Se asigna a "intent" la actividad "UsuarioRegistrado".
                     */
                    //getChatsAndMessages();
                    intent = new Intent(Splash.this, UsuarioRegistrado.class);
                } else {
                    /* No hay una sesión iniciada.
                     * Se asigna a "intent" la actividad "UsuarioNoRegistrado".
                     */
                    intent = new Intent(Splash.this, UsuarioNoRegistrado.class);
                }

            }
        });

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                /* Tras acabar las imágenes del inicio se carga la actividad "UsuarioRegistrado" o
                 * "UsuarioNoRegistrado", dependiendo de si hay una sesión iniciada o no, y finaliza
                 * la actividad del inicio (la de las imágenes).
                 */
                startActivity(intent);
                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);

    }

    // Comprueba si hay una sesión iniciada y devuelve el intent para iniciar la activity que corresponda.
    private boolean sessionExists() {
        SharedPreferences settings = getSharedPreferences("Config", 0);
        boolean session = settings.getBoolean("sesion",false);

        if (session) {
            return true;
        } else {
            return false;
        }

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
                new ChatsHandler(this)
        );
    }
}