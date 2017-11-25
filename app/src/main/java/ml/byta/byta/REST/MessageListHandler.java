package ml.byta.byta.REST;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.Adapters.MessageAdapter;
import ml.byta.byta.Objects.Message;
import ml.byta.byta.Objects.MessagesFromChatResponse;
import ml.byta.byta.R;


public class MessageListHandler extends AsyncHttpResponseHandler {

    private Activity activity;
    private ListView messagesList;

    public MessageListHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        // Respuesta del servidor.
        MessagesFromChatResponse response = gson.fromJson(new String(responseBody), MessagesFromChatResponse.class);

        // Se selecciona la ListView para la lista de mensajes.
        messagesList = (ListView) activity.findViewById(R.id.messages_list);

        // ArrayList que contiene los mensajes.
        List<Message> messages = new ArrayList<>();

        // Se llena el ArrayList anterior con los mensajes enviados por el servidor.
        for (int i = 0; i < response.getCount(); i++) {
            messages.add(response.getMessages().get(i));
        }

        // Se asigna el adaptador a la ListView.
        messagesList.setAdapter(new MessageAdapter(activity, messages));
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Log.d("Main", "Fallado. Código: " + statusCode);
    }

}
