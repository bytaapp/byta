package ml.byta.byta.REST;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.Activities.ChatActivity;
import ml.byta.byta.Adapters.MessageAdapter;
import ml.byta.byta.DataBase.Chat;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Message;
import ml.byta.byta.R;
import ml.byta.byta.Server.Responses.SendNewMessageToChatResponse;

public class SendNewMessageToChatHandler extends AsyncHttpResponseHandler {

    private Activity activity;
    private EditText keyboard;
    private ListView messagesList;
    private Message message;
    private List<Message> messages;
    private Chat chat;

    public SendNewMessageToChatHandler(Activity activity, EditText keyboard, ListView messagesList, Message message) {
        this.activity = activity;
        this.keyboard = keyboard;
        this.messagesList = messagesList;
        this.message = message;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        SendNewMessageToChatResponse response = gson.fromJson(new String(responseBody), SendNewMessageToChatResponse.class);

        if (response.isOk()) {
            // Si el mensaje se ha enviado correctamente se limpia el EditText.
            keyboard.setText("");

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    // Se almacena el nuevo mensaje en la base de datos.
                    Database.db.messageDao().insert(message);

                    Log.d("Main", "-------------------------------------------------------------------");
                    Log.d("Main", "Nuevo mensaje insertado en la BD local");
                    Log.d("Main", "-------------------------------------------------------------------");

                    messages = Database.db.messageDao().getByChatId(message.getChatId());
                    chat = Database.db.chatDao().getByServerId(message.getChatId());

                    if (chat != null && messages.size() > 0) {     // Hay mensajes almacenados.

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.setTitle(activity.getResources().getString(R.string.chat_with_title) + " " + chat.getInterlocutorName());

                                // Se selecciona la ListView para la lista de mensajes.
                                messagesList = (ListView) activity.findViewById(R.id.messages_list);

                                // Se añade una propiedad a la ListView para que haga automáticamente scroll hasta el final de la lista.
                                if (messages.size() > 11) {
                                    messagesList.setStackFromBottom(true);
                                }

                                messagesList.setAdapter(new MessageAdapter(activity, messages));
                            }
                        });

                    }
                }
            });

        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Log.d("Main", "Fallado. Código: " + statusCode);
    }
}
