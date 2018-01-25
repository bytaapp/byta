package ml.byta.byta.EventListeners;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.SyncHttpClient;

import ml.byta.byta.Activities.MisLikes;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Object;
import ml.byta.byta.Server.Handlers.BorrarLikeHandler;
import ml.byta.byta.Server.Handlers.ChatsHandler;

/**
 * Created by maialen on 22/01/18.
 */

public class DeleteFromMisLikesListener implements View.OnClickListener {

    private Object object;
    Activity activity;
    SharedPreferences settings;

    public DeleteFromMisLikesListener(Object object, Activity activity, SharedPreferences settings) {
        this.object = object;
        this.activity=activity;
        this.settings = settings;
    }



    @Override
    public void onClick(View view) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                Log.d("dislike", "Dislike al objeto con descripción: " + object.getDescription());

                // TODO: notificar al servidor.
                SyncHttpClient client = new SyncHttpClient();
                int id_foto = object.getId();

                // Se hace la petición al servidor. Los mensajes se piden en el handler de los chats.
                client.get(
                        activity,
                        "https://byta.ml/apiV2/gestionar_objetos.php?modo=borrar_like&id_objeto="+id_foto+"&sessionID=" + settings.getString("sessionID", ""),
                        new BorrarLikeHandler(activity, object)
                );

                //Refresco la activity para que el usuario vea que el objeto ya no aparece como liked
                /*Intent intent = new Intent(activity, MisLikes.class);
                activity.startActivity(intent);
                activity.finish();*/

            }
        });

    }
}
