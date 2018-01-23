package ml.byta.byta.EventListeners;

import android.util.Log;
import android.view.View;

import ml.byta.byta.DataBase.Object;

public class SuperlikeObjectClickListener implements View.OnClickListener {

    private Object object;

    public SuperlikeObjectClickListener(Object object) {
        this.object = object;
    }

    @Override
    public void onClick(View view) {
        Log.d("superlike", "SuperlikedObject al objeto con descripción: " + object.getDescription());

        // TODO: petición al servidor.
    }

}
