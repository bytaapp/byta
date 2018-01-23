package ml.byta.byta.EventListeners;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;

import link.fls.swipestack.SwipeStack;
import ml.byta.byta.R;

/**
 * Created by ibai on 11/27/17.
 */

public class ClickBotonesSwipe implements View.OnClickListener {

    Activity activity;
    boolean decision;
    int i=0;
    SwipeStack pila_cartas;

    public ClickBotonesSwipe(Activity activity, boolean decision) {
        this.activity = activity;
        this.decision = decision;
        pila_cartas = activity.findViewById(R.id.pila_cartas);
    }

    //Método si es superLike
    public ClickBotonesSwipe(Activity activity, boolean decision, int i) {
        this.activity = activity;
        this.decision = decision;
        this.i=i;
        pila_cartas = activity.findViewById(R.id.pila_cartas);
    }

    @Override
    public void onClick(View v) {

        SharedPreferences settings = activity.getSharedPreferences("Config", 0);
        SharedPreferences.Editor editor = settings.edit();

        if (decision && this.i==1){
            editor.putString("superLike", "si");
            editor.commit();
            pila_cartas.swipeTopViewToRight();
        }else if (decision && this.i==0){
            pila_cartas.swipeTopViewToRight();
        }else{
            pila_cartas.swipeTopViewToLeft();
        }
    }
}
