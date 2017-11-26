package ml.byta.byta.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;

import ml.byta.byta.EventListeners.SendMessageToChatClickListener;
import ml.byta.byta.R;
import ml.byta.byta.REST.MessageListHandler;


// Actividad que muestra el contenido de un chat (los mensajes).

public class ChatActivity extends AppCompatActivity {

    private EditText keyboard;
    private Button sendButton;
    private int id;
    private Handler handler;            // Se necesita para el hilo de refresco.
    private Activity activity = this;   // Se necesita para el hilo de refresco.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        handler = new Handler();

        keyboard = (EditText) findViewById(R.id.keyboard);
        sendButton = (Button) findViewById(R.id.send_button);

        // Se toma el id del chat en el que estamos.
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("chatId");

        /* Se hace una petición al servidor para obtener los mensajes correspondientes a
         * este chat.
         */
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(
                this,
                "https://byta.ml/api/SwappieChat/public/index.php/api/chat/" + id + "/messages",
                new MessageListHandler(this)
        );

        sendButton.setOnClickListener(new SendMessageToChatClickListener(this, keyboard, id));

        // Se refresca la lista de mensajes cada 3 segundos.
        refreshMessages(handler, 3000);
    }

    private void refreshMessages(final Handler handler, final int miliseconds) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Para hacer la petición de los mensajes de este chat se necesita el token de la sesión.
                SharedPreferences settings = getSharedPreferences("Config", 0);

                // Se hace una petición.
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(
                        activity,
                        "https://byta.ml/api/SwappieChat/public/index.php/api/chat/" + id + "/messages",
                        new MessageListHandler(activity)
                );

                handler.postDelayed(this, miliseconds);

                Log.d("Main", "Mensajes pedidos");
            }
        }, miliseconds);
    }

    @Override
    public void onBackPressed() {
        // Se detiene el hilo de ejecución.
        handler.removeCallbacksAndMessages(null);

        super.onBackPressed();
    }
}
