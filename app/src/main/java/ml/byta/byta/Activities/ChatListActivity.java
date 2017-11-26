package ml.byta.byta.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;

import ml.byta.byta.R;
import ml.byta.byta.REST.ChatListHandler;


public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        // Se hace una petición asíncrona para obtener la lista de chats.
        AsyncHttpClient client = new AsyncHttpClient();

        // Se toma el ID del usuario.
        SharedPreferences settings = getSharedPreferences("Config", 0);
        int id = settings.getInt("id", 0);

        // Se hace la petición al servidor.
        client.get(
                this,
                "https://byta.ml/api/SwappieChat/public/index.php/api/chats/" + id + "",
                new ChatListHandler(this)
        );

    }
}
