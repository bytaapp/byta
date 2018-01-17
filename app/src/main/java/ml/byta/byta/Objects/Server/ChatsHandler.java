package ml.byta.byta.Objects.Server;

import android.app.Activity;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.DataBase.AppDatabase;
import ml.byta.byta.DataBase.Chat;

public class ChatsHandler extends AsyncHttpResponseHandler {

    private Activity activity;
    private AppDatabase db;

    public ChatsHandler(Activity activity, AppDatabase db) {
        this.activity = activity;
        this.db = db;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        /* LA RESPUESTA DEL SERVIDOR TAMBIÉN LLEVA OBJETOS POR SI HAY QUE UTILIZARLOS.
         * OBJETOS CUYO MATCH HA GENERADO EL CHAT.
         */

        // Respuesta del servidor.
        ChatsResponse response = gson.fromJson(new String(responseBody), ChatsResponse.class);

        if (response.getChats().size() > 0) {
            // Como aquí ya estamos en un hilo independiente del principal, supongo que no hay que crear otro hilo (COMPROBAR POR SI ACASO).

            // Se eliminan todos los chats que haya almacenados en la base de datos local.
            db.chatDao().deleteAllChats();

            List<Chat> chats = new ArrayList<>();

            for (int i = 0; i < response.getChats().size(); i++) {
                Chat chat = null;

                SharedPreferences settings = activity.getSharedPreferences("Config", 0);

                // Para que la columna "firstUserId" siempre tenga mi ID.
                if (response.getChats().get(i).getUsers().get(0).getId() == settings.getInt("id", 0)) {
                    chat = new Chat(
                            response.getChats().get(i).getUsers().get(0).getId(),
                            response.getChats().get(i).getUsers().get(1).getId(),
                            response.getChats().get(i).getServerId()
                    );
                } else {
                    chat = new Chat(
                            response.getChats().get(i).getUsers().get(1).getId(),
                            response.getChats().get(i).getUsers().get(0).getId(),
                            response.getChats().get(i).getServerId()
                    );
                }

                chats.add(chat);
            }

            // Para cada chat se piden sus mensajes de forma asíncrona.
            for (int i = 0; i < chats.size(); i++) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(
                        activity,
                        "https://byta.ml/api/SwappieChat/public/index.php/api/chat/" + chats.get(i).getServerId() + "/messages",
                        new MessagesHandler(db)
                );
            }

            // Se almacenan los chats recibidos en la base de datos local.
            db.chatDao().insertChats(chats);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        // TRATAR EL FALLO, ¿CÓMO?
    }
}
