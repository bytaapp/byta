package ml.byta.byta.REST;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.Objects.SendNewMessageToChatResponse;

public class SendNewMessageToChatHandler extends AsyncHttpResponseHandler {

    private Activity activity;
    private EditText keyboard;
    private int id;             // ID del chat en el que estamos.
    private ListView messagesList;

    public SendNewMessageToChatHandler(Activity activity, EditText keyboard, int id) {
        this.activity = activity;
        this.keyboard = keyboard;
        this.id = id;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        SendNewMessageToChatResponse response = gson.fromJson(new String(responseBody), SendNewMessageToChatResponse.class);

        // Si el mensaje se ha enviado correctamente se limpia el EditText.
        keyboard.setText("");

        /* Se piden todos los mensajes de este chat para que se muestre el mensaje que
         * se acaba de enviar.
         */
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(activity, "https://api.swappie.tk/api/SwappieChat/public/index.php/api/chat/" + id + "/messages", new MessageListHandler(activity));

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Log.d("Main", "Fallado. Código: " + statusCode);
    }
}
