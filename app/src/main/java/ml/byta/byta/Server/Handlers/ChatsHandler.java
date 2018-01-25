package ml.byta.byta.Server.Handlers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.DataBase.AppDatabase;
import ml.byta.byta.DataBase.Chat;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.Server.Responses.ChatsResponse;

public class ChatsHandler extends AsyncHttpResponseHandler {

    private Activity activity;

    public ChatsHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        /* LA RESPUESTA DEL SERVIDOR TAMBIÉN LLEVA OBJETOS POR SI HAY QUE UTILIZARLOS.
         * OBJETOS CUYO MATCH HA GENERADO EL CHAT.
         */

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Respuesta del servidor al pedir los chats --> " + new String(responseBody));
        Log.d("Main", "-------------------------------------------------------------------");

        // Respuesta del servidor.
        ChatsResponse response = gson.fromJson(new String(responseBody), ChatsResponse.class);

        if (response.isOk() && response.getChats().size() > 0) {

            // Se eliminan todos los chats que haya almacenados en la base de datos local.
            Database.db.chatDao().deleteAllChats();

            List<Chat> chats = new ArrayList<>();

            SharedPreferences settings = activity.getSharedPreferences("Config", 0);

            for (int i = 0; i < response.getChats().size(); i++) {
                Chat chat = null;

                // Para que la columna "firstUserId" siempre tenga mi ID.
                if (response.getChats().get(i).getUsers().get(0).getId() == settings.getInt("userID", 0)) {
                    chat = new Chat(
                            response.getChats().get(i).getUsers().get(0).getId(),
                            response.getChats().get(i).getUsers().get(1).getId(),
                            response.getChats().get(i).getUsers().get(1).getName(),
                            response.getChats().get(i).getServerId()
                    );
                } else {
                    chat = new Chat(
                            response.getChats().get(i).getUsers().get(1).getId(),
                            response.getChats().get(i).getUsers().get(0).getId(),
                            response.getChats().get(i).getUsers().get(0).getName(),
                            response.getChats().get(i).getServerId()
                    );
                }

                chats.add(chat);
            }

            // Se almacenan los chats recibidos en la base de datos local.
            Database.db.chatDao().insertChats(chats);

            // Se eliminan todos los mensajes que haya almacenados en la base de datos local.
            Database.db.messageDao().deleteAllMessages();

            // Para cada chat se piden sus mensajes de forma asíncrona.
            for (int i = 0; i < chats.size(); i++) {
                SyncHttpClient client = new SyncHttpClient();
                client.get(
                        activity,
                        "https://byta.ml/apiV2/BytaChat/public/index.php/api/chat/" + chats.get(i).getServerId() + "/" + settings.getString("sessionID", "") + "/messages",
                        new MessagesHandler()
                );
            }

        } else {
            // "ok" es false o no se han enviado chats.
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        // TODO: TRATAR EL FALLO, ¿CÓMO?
        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "ERROR --> Ha entrado en onFailure");
        Log.d("Main", "Código de error --> " + statusCode);
        Log.d("Main", "Throwable error --> " + error);
        Log.d("Main", "-------------------------------------------------------------------");
    }
}
