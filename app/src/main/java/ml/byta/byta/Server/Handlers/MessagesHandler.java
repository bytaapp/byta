package ml.byta.byta.Server.Handlers;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.DataBase.Chat;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Message;
import ml.byta.byta.Server.Responses.MessagesResponse;

public class MessagesHandler extends AsyncHttpResponseHandler {

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        /*
        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Respuesta del servidor al pedir los mensajes de un chat --> " + new String(responseBody));
        Log.d("Main", "-------------------------------------------------------------------");
        */

        // Respuesta del servidor.
        MessagesResponse response = gson.fromJson(new String(responseBody), MessagesResponse.class);

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Respuesta del servidor al pedir los mensajes --> " + new String(responseBody));
        Log.d("Main", "-------------------------------------------------------------------");

        // Se eliminan todos los mensajes que haya almacenados en la base de datos local.
        Database.db.messageDao().deleteAllMessages();

        if (response.isOk() && response.getMessages().size() > 0) {

            List<Message> messages = new ArrayList<>();

            for (int i = 0; i < response.getMessages().size(); i++) {
                messages.add(response.getMessages().get(i));
            }

            // Se almacenan los mensajes recibidos en la base de datos local.
            Database.db.messageDao().insertMessages(messages);

        } else {
            // "ok" es false o no se han enviado mensajes.
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
