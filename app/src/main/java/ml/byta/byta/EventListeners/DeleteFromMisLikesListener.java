package ml.byta.byta.EventListeners;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import ml.byta.byta.Activities.MisLikes;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Object;

/**
 * Created by maialen on 22/01/18.
 */

public class DeleteFromMisLikesListener implements View.OnClickListener {

    private Object object;
    Activity activity;

    public DeleteFromMisLikesListener(Object object, Activity activity) {
        this.object = object;
        this.activity=activity;
    }



    @Override
    public void onClick(View view) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                // Se elimina el objeto de la base de datos.
                Database.db.objectDao().delete(object);
                Log.d("dislike", "Dislike al objeto con descripción: " + object.getDescription());

                // TODO: notificar al servidor.

                //Refresco la activity para que el usuario vea que el objeto ya no aparece como liked
                Intent intent = new Intent(activity, MisLikes.class);
                activity.startActivity(intent);
                activity.finish();

            }
        });

    }
}
