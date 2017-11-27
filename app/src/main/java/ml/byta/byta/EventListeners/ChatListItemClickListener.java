package ml.byta.byta.EventListeners;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;

import java.util.List;
import ml.byta.byta.R;

import ml.byta.byta.Activities.ChatActivity;
import ml.byta.byta.Objects.Chat;

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
        intent.putExtra("chatId", chats.get(position).getId());

        SharedPreferences settings = activity.getSharedPreferences("Config", 0);

        // En ChatActivity.java se necesita el nombre de la otra persona para ponerlo en el título.
        if (chats.get(position).getUsers().get(0).getId() == settings.getInt("id", 0)) {
            intent.putExtra("receptor", chats.get(position).getUsers().get(1).getName());
        } else if (chats.get(position).getUsers().get(1).getId() == settings.getInt("id", 0)) {
            intent.putExtra("receptor", chats.get(position).getUsers().get(0).getName());
        } else {
            intent.putExtra("receptor", activity.getResources().getString(R.string.unknown_receptor));
        }

        this.activity.startActivity(intent);
    }

}
