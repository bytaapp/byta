package ml.byta.byta.Server.Handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import ml.byta.byta.Activities.ChatListActivity;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Object;
import ml.byta.byta.R;
import ml.byta.byta.Server.Responses.SwipesResponse;

public class SwipesHandler extends AsyncHttpResponseHandler {

    Activity activity;
    private boolean decission;
    private int id;

    public SwipesHandler(Activity activity, boolean decission, int id) {
        this.activity = activity;
        this.decission = decission;
        this.id = id;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        SwipesResponse response = gson.fromJson(new String(responseBody), SwipesResponse.class);

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Respuesta del servidor al swipe --> " + gson.toJson(response));
        Log.d("Main", "-------------------------------------------------------------------");

        if (response.isOk()) {  // Respuesta del servidor correcta.

            if (decission == false) {
                // Se elimina el objeto de la base de datos local.
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Database.db.objectDao().deleteByServerId(id);
                    }
                });
            } else if (decission == true) {
                // Se actualiza el objeto en la base de datos local con "viewed" = true.
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Object object = Database.db.objectDao().getByServerId(id);
                        object.setViewed(true);
                        Database.db.objectDao().update(object);
                    }
                });

                if (response.getError().equals("match")) {
                    //CARGA POPUP MATCH
                    LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.match, (ViewGroup) activity.findViewById(R.id.match_element));

                    final PopupWindow popUpWindow = new PopupWindow(layout, DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT, true);
                    popUpWindow.showAtLocation(layout, Gravity.TOP, 0, 0);

                    //BOTÓN PARA SEGUIR HACIENDO SWIPE, CIERRA EL POPUP
                    Button cancelButton = (Button) layout.findViewById(R.id.buttonSeguir);

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popUpWindow.dismiss();
                        }
                    });

                    //cancelButton.setOnClickListener(cancel_button_click_listener);

                    //AQUÍ VA EL BOTON PARA ABRIR CHAT, AÚN NO HACE NADA
                    Button buttonIniciarChat = (Button) layout.findViewById(R.id.buttonChat);

                    buttonIniciarChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(activity, ChatListActivity.class);
                            activity.startActivity(intent);
                            popUpWindow.dismiss();
                        }
                    });

                    //buttonIniciarChat.setOnClickListener(button_chat_click_listener);
                }

            }

        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        // TODO: TRATAR EL FALLO, ¿CÓMO?
        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "ERROR --> Ha entrado en onFailure");
        Log.d("Main", "Código de error --> " + statusCode);
        Log.d("Main", "Throwable error --> " + error);
        Log.d("Main", "-------------------------------------------------------------------");
    }

}
