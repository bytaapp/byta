package ml.byta.byta.Adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ml.byta.byta.Objects.Chat;
import ml.byta.byta.R;


public class ChatAdapter extends BaseAdapter {

    private Activity activity;
    private List<Chat> chats;

    public ChatAdapter(Activity activity, List<Chat> chats) {
        this.activity = activity;
        this.chats = chats;
    }

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

        // Se toma el ID del usuario.
        SharedPreferences settings = activity.getSharedPreferences("Config", 0);
        int id = settings.getInt("id", 0);

        /* Si el ID del primer usuario del chat es mi ID, significa que ese usuario soy yo, por lo que
         * se asignará al nombre del otro usuario en el nombre de contacto. En caso contrario, se asignará
         * el nombre del primer usuario.
         */
        if (chat.getUsers().get(0).getId() == settings.getInt("id", 0)) {
            contactName.setText(chat.getUsers().get(1).getName());
        } else {
            contactName.setText(chat.getUsers().get(0).getName());
        }

        return convertView;
    }

}
