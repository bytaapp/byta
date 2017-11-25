package ml.byta.byta.REST;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.Adapters.ChatAdapter;
import ml.byta.byta.EventListeners.ChatListItemClickListener;
import ml.byta.byta.Objects.Chat;
import ml.byta.byta.Objects.ChatListResponse;
import ml.byta.byta.R;


public class ChatListHandler extends AsyncHttpResponseHandler {

    private Activity activity;
    private ListView chatList;
    private TextView text;
    private ImageView im;

    public ChatListHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        // Respuesta del servidor.
        ChatListResponse response = gson.fromJson(new String(responseBody), ChatListResponse.class);

        if (response.getChats().size()==0){
            chatList = (ListView) activity.findViewById(R.id.chat_list);
            chatList.setVisibility(View.GONE);
            text = (TextView)activity.findViewById(R.id.no_chat);
            text.setVisibility(View.VISIBLE);
            im = (ImageView)activity.findViewById(R.id.sorry);
            im.setVisibility(View.VISIBLE);

        }else{

            // Se selecciona la ListView para la lista de chats.
            chatList = (ListView) activity.findViewById(R.id.chat_list);
            chatList.setVisibility(View.VISIBLE);
            text = (TextView)activity.findViewById(R.id.no_chat);
            text.setVisibility(View.GONE);
            im = (ImageView)activity.findViewById(R.id.sorry);
            im.setVisibility(View.GONE);

            // ArrayList que contiene los chats.
            List<Chat> chats = new ArrayList<>();

            // Se llena el ArrayList anterior con los chats enviados por el servidor.
            for (int i = 0; i < response.getCount(); i++) {
                chats.add(response.getChats().get(i));
            }

            // Se asigna el adaptador a la ListView.
            chatList.setAdapter(new ChatAdapter(activity, chats));

            // Listener para abrir cada chat.
            chatList.setOnItemClickListener(new ChatListItemClickListener(activity, chats));
        }



    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        error.printStackTrace(System.out);
    }

}
