package ml.byta.byta.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;

import java.util.List;

import ml.byta.byta.Adapters.MessageAdapter;
import ml.byta.byta.DataBase.Chat;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Message;
import ml.byta.byta.EventListeners.SendMessageToChatClickListener;
import ml.byta.byta.R;
import ml.byta.byta.REST.MessageListHandler;


// Actividad que muestra el contenido de un chat (los mensajes).

public class ChatActivity extends AppCompatActivity {

    private EditText keyboard;
    private ImageButton sendButton;
    private String receptor;
    private int chatId;
    private ListView messagesList;
    private Handler handler;            // Se necesita para el hilo de refresco.
    private Activity activity = this;   // Se necesita para el hilo de refresco.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        handler = new Handler();

        keyboard = (EditText) findViewById(R.id.keyboard);
        sendButton = (ImageButton) findViewById(R.id.send_button);

        Bundle bundle = getIntent().getExtras();
        chatId = bundle.getInt("chatID");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<Message> messages = Database.db.messageDao().getByChatId(chatId);
                Chat chat = Database.db.chatDao().getByServerId(chatId);

                if (chat != null) {     // Hay mensajes almacenados.

                    setTitle(getResources().getString(R.string.chat_with_title) + " " + chat.getInterlocutorName());

                    // Se selecciona la ListView para la lista de mensajes.
                    messagesList = (ListView) activity.findViewById(R.id.messages_list);

                    // Se añade una propiedad a la ListView para que haga automáticamente scroll hasta el final de la lista.
                    if (messages.size() > 11) {
                        messagesList.setStackFromBottom(true);
                    }

                    messagesList.setAdapter(new MessageAdapter(ChatActivity.this, messages));

                } else {    // No hay mensajes almacenados.

                }
            }
        });

        /*
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(
                this,
                "https://byta.ml/api/SwappieChat/public/index.php/api/chat/" + id + "/messages",
                new MessageListHandler(this)
        );

        sendButton.setOnClickListener(new SendMessageToChatClickListener(this, keyboard, id));

        // Se refresca la lista de mensajes cada 3 segundos.
        refreshMessages(handler, 3000);
        */
    }

    private void refreshMessages(final Handler handler, final int miliseconds) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Se hace una petición.
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(
                        activity,
                        "https://byta.ml/api/SwappieChat/public/index.php/api/chat/" + chatId + "/messages",
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
