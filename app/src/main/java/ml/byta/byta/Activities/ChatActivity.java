package ml.byta.byta.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        keyboard = (EditText) findViewById(R.id.keyboard);
        sendButton = (Button) findViewById(R.id.send_button);

        // Se toma el id del chat en el que estamos.
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("chatId");

        /* Se hace una petición al servidor para obtener los mensajes correspondientes a
         * este chat.
         */
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(this, "https://api.swappie.tk/api/SwappieChat/public/index.php/api/chat/" + id + "/messages", new MessageListHandler(this));

        sendButton.setOnClickListener(new SendMessageToChatClickListener(this, keyboard, id));
    }
}
