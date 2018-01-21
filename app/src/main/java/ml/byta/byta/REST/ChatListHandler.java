package ml.byta.byta.REST;

import android.app.Activity;
import android.graphics.Bitmap;
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

        // Se selecciona la ListView para la lista de chats.
        chatList = (ListView) activity.findViewById(R.id.chat_list);

        // Se selecciona el TextView que indica que no hay chats.
        text = (TextView) activity.findViewById(R.id.no_chat);

        // Se selecciona la ImageView que indica que no hay chats.
        //im = (ImageView) activity.findViewById(R.id.sorry);

        if (response.getChats().size() == 0) {
            // Se indica al usuario que no hay chats.
            chatList.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
            im.setVisibility(View.VISIBLE);
        } else {
            // Sí que hay chats.
            chatList.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);
            im.setVisibility(View.GONE);

            // ArrayList que contiene los chats.
            List<Chat> chats = new ArrayList<>();

            // ArrayList que contiene objetos que relacionan imágenes de objetos y el chat al que pertenecen.
            List<ParChatIdBitmap> pares = new ArrayList<>();

            // Se llena el ArrayList anterior con los chats enviados por el servidor.
            for (int i = 0; i < response.getCount(); i++) {
                chats.add(response.getChats().get(i));

                // Para cada chat, se piden las imágenes de los objetos asociados a él.
                for (int j = 0; j < response.getChats().get(i).getObjects().size(); j++) {
                    // ID del objeto cuya imagen se quiere descargar.
                    int objectId = response.getChats().get(i).getObjects().get(j).getId();

                    // objectBitmap contiene la imagen del objeto.
                    Bitmap objectBitmap = ClasePeticionRest.downloadBitmap(String.valueOf(objectId));

                    // ID del chat al que pertenece el objeto.
                    int chatId = response.getChats().get(i).getId();

                    pares.add(new ParChatIdBitmap(chatId, objectBitmap));
                }

            }

            // Se asigna el adaptador a la ListView.
            //chatList.setAdapter(new ChatAdapter(activity, chats, pares));

            // Listener para abrir cada chat.
            //chatList.setOnItemClickListener(new ChatListItemClickListener(activity, chats));
        }



    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        error.printStackTrace(System.out);
    }

    public class ParChatIdBitmap {

        private int chatId;
        private Bitmap bitmap;

        public ParChatIdBitmap(int chatId, Bitmap bitmap) {
            this.chatId = chatId;
            this.bitmap = bitmap;
        }

        public int getChatId() {
            return chatId;
        }

        public void setChatId(int chatId) {
            this.chatId = chatId;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

    }

}
