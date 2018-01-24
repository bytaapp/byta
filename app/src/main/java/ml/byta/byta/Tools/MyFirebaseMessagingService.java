package ml.byta.byta.Tools;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by maialen on 11/12/17.
 */

public class MyFirebaseMessagingService  extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData());
        Log.d("entro","notificacion");

        if (remoteMessage.getNotification().getTitle().equals("Tienes un nuevo Match!") || remoteMessage.getNotification().getTitle().equals("¡Has recibido un SuperLike!")){

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Tienes un nuevo Match", Toast.LENGTH_SHORT).show();
                }
            });

        }else if(remoteMessage.getNotification().getTitle().equals("¡Tienes un nuevo mensaje!")){

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Tienes nuevos mensajes", Toast.LENGTH_SHORT).show();
                }
            });


        }




    }

}
