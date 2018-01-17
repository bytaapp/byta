package ml.byta.byta.Objects.Server;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.DataBase.AppDatabase;
import ml.byta.byta.DataBase.Message;

public class MessagesHandler extends AsyncHttpResponseHandler {

    private AppDatabase db;

    public MessagesHandler(AppDatabase db) {
        this.db = db;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        // Respuesta del servidor.
        MessagesFromChatResponse response = gson.fromJson(new String(responseBody), MessagesFromChatResponse.class);

        if (response.getMessages().size() > 0) {

            // Se eliminan todos los mensajes que haya almacenados en la base de datos local.
            db.messageDao().deleteAllMessages();

            List<Message> messages = new ArrayList<>();

            for (int i = 0; i < response.getMessages().size(); i++) {
                messages.add(response.getMessages().get(i));
            }

            // Se almacenan los mensajes recibidos en la base de datos local.
            db.messageDao().insertMessages(messages);

        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        // TRATAR EL FALLO, ¿CÓMO?
    }
}
