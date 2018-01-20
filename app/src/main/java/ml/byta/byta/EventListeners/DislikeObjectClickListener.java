package ml.byta.byta.EventListeners;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import ml.byta.byta.Activities.MisLikes;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Object;

public class DislikeObjectClickListener implements View.OnClickListener {

    private Object object;

    public DislikeObjectClickListener(Object object) {
        this.object = object;
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

            }
        });

    }

}
