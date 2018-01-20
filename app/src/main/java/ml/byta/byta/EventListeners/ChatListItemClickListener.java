package ml.byta.byta.EventListeners;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;

import java.util.List;

import ml.byta.byta.DataBase.Chat;
import ml.byta.byta.R;

import ml.byta.byta.Activities.ChatActivity;

public class ChatListItemClickListener implements AdapterView.OnItemClickListener {

    private Activity activity;
    private List<Chat> chats;

    public ChatListItemClickListener(Activity activity, List<Chat> chats) {
        this.activity = activity;
        this.chats = chats;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this.activity, ChatActivity.class);

        // Para pedir los mensajes de un chat se necesita el ID del chat.
        intent.putExtra("chatID", chats.get(position).getServerId());

        this.activity.startActivity(intent);
    }

}
