package ml.byta.byta.Activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;

import ml.byta.byta.Adapters.ChatAdapter;
import ml.byta.byta.DataBase.Chat;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Message;
import ml.byta.byta.EventListeners.ChatListItemClickListener;
import ml.byta.byta.R;
import ml.byta.byta.REST.ChatListHandler;


public class ChatListActivity extends AppCompatActivity {

    private ListView chatList;
    private TextView noChatsText;
    private ImageView noChatsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        // Se selecciona la ListView para la lista de chats.
        chatList = (ListView) findViewById(R.id.chat_list);

        // Se selecciona el TextView que indica que no hay chats.
        noChatsText = (TextView) findViewById(R.id.no_chat);

        // Se selecciona la ImageView que indica que no hay chats.
        //noChatsImage = (ImageView) findViewById(R.id.sorry);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final List<Chat> chats = Database.db.chatDao().getAllChats();
                final List<Message> lastMessages = new ArrayList<>();

                // "lastMessages" contiene el último mensaje de cada chat del array "chats".
                for (int i = 0; i < chats.size(); i++) {
                    lastMessages.add(Database.db.messageDao().getLastMessageFromChat(chats.get(i).getServerId()));
                }

                if (chats != null) {    // Hay chats almacenados.

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chatList.setVisibility(View.VISIBLE);
                            noChatsText.setVisibility(View.GONE);
                            //noChatsImage.setVisibility(View.GONE);

                            // Se asigna el adaptador a la ListView.
                            chatList.setAdapter(new ChatAdapter(ChatListActivity.this, chats, lastMessages));

                            // Listener para abrir cada chat.
                            chatList.setOnItemClickListener(new ChatListItemClickListener(ChatListActivity.this, chats));
                        }
                    });


                } else {    // No hay chats almacenados.

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chatList.setVisibility(View.GONE);
                            noChatsText.setVisibility(View.VISIBLE);
                            //noChatsImage.setVisibility(View.VISIBLE);

                        }
                    });



                }
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Por si se ha escrito algún mensaje, para que salga como último mensaje del chat.
        recreate();
    }
}
