package ml.byta.byta.EventListeners;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import cz.msebera.android.httpclient.entity.StringEntity;
import ml.byta.byta.DataBase.Message;
import ml.byta.byta.REST.ClasePeticionRest;
import ml.byta.byta.Server.Handlers.SendNewMessageToChatHandler;
import ml.byta.byta.Server.Requests.SendNewMessageToChatRequest;


public class SendMessageToChatClickListener implements View.OnClickListener {

    private Activity activity;
    private EditText keyboard;
    private ListView messagesList;
    private int chatId;             // ID del chat al que se quiere enviar el mensaje.

    public SendMessageToChatClickListener(Activity activity, EditText keyboard, ListView messagesList, int chatId) {
        this.activity = activity;
        this.keyboard = keyboard;
        this.messagesList = messagesList;
        this.chatId = chatId;
    }

    @Override
    public void onClick(View v) {

        // Se comprueba si hay algo escrito en el teclado.
        if (keyboard.getText().toString().equals("")) {
            Toast.makeText(activity, "No puedes enviar un mensaje en blanco", Toast.LENGTH_SHORT).show();
        } else {
            Gson gson = new Gson();

            SharedPreferences settings = activity.getSharedPreferences("Config", 0);

            long timestamp = System.currentTimeMillis()/1000L;

            Message message = new Message(chatId, settings.getInt("userID", 0), keyboard.getText().toString(), timestamp, 0);

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(
                    activity,
                    "https://byta.ml/apiV2/BytaChat/public/index.php/api/chat/" + chatId + "/message",
                    new StringEntity(gson.toJson(new SendNewMessageToChatRequest(settings.getInt("userID", 0), keyboard.getText().toString(), timestamp)), "UTF-8"),
                    "application/json",
                    new SendNewMessageToChatHandler(activity, chatId, keyboard, messagesList, message)
            );

            // Notificación de un nuevo mensaje.
            new ClasePeticionRest.NotificarMensaje(this.activity,chatId,settings.getInt("userID", 0), keyboard.getText().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }

    }

}
