package ml.byta.byta.EventListeners;

import android.app.Activity;
import android.view.View;

import link.fls.swipestack.SwipeStack;
import ml.byta.byta.R;

/**
 * Created by ibai on 11/27/17.
 */

public class ClickBotonesSwipe implements View.OnClickListener {

    Activity activity;
    boolean decision;
    SwipeStack pila_cartas;

    public ClickBotonesSwipe(Activity activity, boolean decision) {
        this.activity = activity;
        this.decision = decision;
        pila_cartas = activity.findViewById(R.id.pila_cartas);
    }

    @Override
    public void onClick(View v) {
        if (decision){
            pila_cartas.swipeTopViewToRight();
        }else{
            pila_cartas.swipeTopViewToLeft();
        }
    }
}
