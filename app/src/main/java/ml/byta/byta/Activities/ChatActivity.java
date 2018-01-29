package ml.byta.byta.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        keyboard = (EditText) findViewById(R.id.keyboard);
        sendButton = (ImageButton) findViewById(R.id.send_button);

        // Se toma el ID del chat cuya pantalla se ha abierto.
        Bundle bundle = getIntent().getExtras();
        chatId = bundle.getInt("chatID");

        // Se llama al método que muestra los mensajes en pantalla.
        showMessages();

        // Listener para el botón de enviar un nuevo mensaje.
        sendButton.setOnClickListener(new SendMessageToChatClickListener(this, keyboard, messagesList, chatId));

        // Se refresca la lista de mensajes cada 3 segundos.
        //refreshMessages(handler, 3000);

    }

    private void refreshMessages(final Handler handler, final int miliseconds) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                List<Message> messages = Database.db.messageDao().getByChatId(chatId);
                Chat chat = Database.db.chatDao().getByServerId(chatId);

                if (chat != null) {     // Hay mensajes almacenados.
                    setTitle(getResources().getString(R.string.chat_with_title) + " " + chat.getInterlocutorName());

                    // Se selecciona la ListView para la lista de mensajes.
                    messagesList = (ListView) findViewById(R.id.messages_list);

                    // Se añade una propiedad a la ListView para que haga automáticamente scroll hasta el final de la lista.
                    if (messages.size() > 10) {
                        messagesList.setStackFromBottom(true);
                    }

                    messagesList.setAdapter(new MessageAdapter(ChatActivity.this, messages));

                } else {    // No hay mensajes almacenados.

                }

            }
        }, miliseconds);
    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }

    private void showMessages() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<Message> messages = Database.db.messageDao().getByChatId(chatId);
                Chat chat = Database.db.chatDao().getByServerId(chatId);

                if (chat != null) {     // Hay mensajes almacenados.
                    setTitle(getResources().getString(R.string.chat_with_title) + " " + chat.getInterlocutorName());

                    // Se selecciona la ListView para la lista de mensajes.
                    messagesList = (ListView) findViewById(R.id.messages_list);

                    // Se añade una propiedad a la ListView para que haga automáticamente scroll hasta el final de la lista.
                    if (messages.size() > 11) {
                        messagesList.setStackFromBottom(true);
                    }

                    messagesList.setAdapter(new MessageAdapter(ChatActivity.this, messages));

                } else {    // No hay mensajes almacenados.

                }
            }
        });
    }
}
