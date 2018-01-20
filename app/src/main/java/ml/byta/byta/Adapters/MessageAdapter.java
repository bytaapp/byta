package ml.byta.byta.Adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ml.byta.byta.DataBase.Message;
import ml.byta.byta.R;


// Adaptador para los mensajes de un chat.

public class MessageAdapter extends BaseAdapter {

    private Activity activity;
    private List<Message> messages;

    public MessageAdapter(Activity activity, List<Message> messages) {
        this.activity = activity;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return this.messages.size();
    }

    @Override
    public Message getItem(int position) {
        return this.messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = this.getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_message, null, false);
        }

        RelativeLayout parentLayout = (RelativeLayout) convertView.findViewById(R.id.message_text_relative_layout);

        TextView messageText = (TextView) convertView.findViewById(R.id.message_text);

        // Se toma el ID del usuario para comprobar quién ha escrito el mensaje.
        SharedPreferences settings = activity.getSharedPreferences("Config", 0);

        // Si yo escribí el mensaje, alinear a la derecha. En caso contrario, alinear a la izquierda.
        if (message.getAuthorId() == settings.getInt("userID", 0)) {
            parentLayout.setGravity(Gravity.RIGHT);
            messageText.setBackgroundResource(R.drawable.rounded_rectangle_2);
        } else {
            parentLayout.setGravity(Gravity.LEFT);
            messageText.setBackgroundResource(R.drawable.rounded_rectangle);
        }

        messageText.setText(message.getText());

        return convertView;
    }

}
