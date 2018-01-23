package ml.byta.byta.Adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ml.byta.byta.DataBase.Chat;
import ml.byta.byta.DataBase.ChatDao;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Message;
import ml.byta.byta.R;
import ml.byta.byta.REST.ChatListHandler;


public class ChatAdapter extends BaseAdapter {

    private Activity activity;
    private List<Chat> chats;
    private List<Message> lastMessages;
    private List<ChatListHandler.ParChatIdBitmap> pares;

    public ChatAdapter(Activity activity, List<Chat> chats, List<Message> lastMessages) {
        this.activity = activity;
        this.chats = chats;
        this.lastMessages = lastMessages;
    }

    /*
    public ChatAdapter(Activity activity, List<Chat> chats, List<ChatListHandler.ParChatIdBitmap> pares) {
        this.activity = activity;
        this.chats = chats;
        this.pares = pares;
    }
    */

    @Override
    public int getCount() {
        return this.chats.size();
    }

    @Override
    public Chat getItem(int position) {
        return this.chats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Chat chat = this.getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_chat, null, false);
        }

        TextView contactName = (TextView) convertView.findViewById(R.id.contact_name);
        TextView lastMessageInChat = (TextView) convertView.findViewById(R.id.last_message_in_chat);
        //ImageView firstObjectChat = (ImageView) convertView.findViewById(R.id.first_object_chat);
        //ImageView secondObjectChat = (ImageView) convertView.findViewById(R.id.second_object_chat);
        //ImageView thirdObjectChat = (ImageView) convertView.findViewById(R.id.third_object_chat);

        contactName.setText(chat.getInterlocutorName());

        if (lastMessages.size() > 0 && position < lastMessages.size()) {
            Message lastMessage = lastMessages.get(position);
            if (lastMessage!=null){
                lastMessageInChat.setText(lastMessage.getText());
            }

        }

        /*
        // Se asignan las imágenes al chat.
        for (int i = 0; i < pares.size(); i++) {
            if (pares.get(i).getChatId() == chat.getId()) {
                // El par contiene una imagen de este chat.

                // Se comprueba si hay ImageView libres (sin imagen aún asignada).
                if (firstObjectChat.getDrawable() == null) {
                    firstObjectChat.setImageBitmap(pares.get(i).getBitmap());
                } else if (secondObjectChat.getDrawable() == null) {
                    secondObjectChat.setImageBitmap(pares.get(i).getBitmap());
                } else if (thirdObjectChat.getDrawable() == null) {
                    thirdObjectChat.setImageBitmap(pares.get(i).getBitmap());
                } else {
                    // Los 3 ImageView ya están ocupados.
                    Log.d("Main", "Los 3 ImageView del chat " + chat.getId() + " ya están ocupados");
                }

            }
        }
        */

        return convertView;
    }

}
