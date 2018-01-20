package ml.byta.byta.Server.Handlers;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.DataBase.AppDatabase;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Message;
import ml.byta.byta.Server.Responses.MessagesFromChatResponse;

public class MessagesHandler extends AsyncHttpResponseHandler {

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        // Respuesta del servidor.
        MessagesFromChatResponse response = gson.fromJson(new String(responseBody), MessagesFromChatResponse.class);

        if (response.isOk() && response.getMessages().size() > 0) {

            // Se eliminan todos los mensajes que haya almacenados en la base de datos local.
            Database.db.messageDao().deleteAllMessages();

            List<Message> messages = new ArrayList<>();

            for (int i = 0; i < response.getMessages().size(); i++) {
                messages.add(response.getMessages().get(i));
            }

            // Se almacenan los mensajes recibidos en la base de datos local.
            Database.db.messageDao().insertMessages(messages);

        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        // TODO: TRATAR EL FALLO, ¿CÓMO?
    }
}
