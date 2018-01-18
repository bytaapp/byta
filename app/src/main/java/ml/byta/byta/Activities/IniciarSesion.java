package ml.byta.byta.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

import ml.byta.byta.R;
import ml.byta.byta.REST.ClasePeticionRest;
import ml.byta.byta.Server.Handlers.LoginHandler;

public class IniciarSesion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static boolean esMailValido(CharSequence objetivo) {
        return objetivo != null && Patterns.EMAIL_ADDRESS.matcher(objetivo).matches();
    }

    public void iniciarSesion(View view){

        SharedPreferences settings = getSharedPreferences("Config", 0);

        EditText edit = (EditText)findViewById(R.id.editText);
        EditText edit2 = (EditText)findViewById(R.id.editText2);

        String email = edit.getText().toString();
        String password = edit2.getText().toString();

        if (esMailValido(email)) {
            AsyncHttpClient client = new AsyncHttpClient();

            // Se hace la petición al servidor.
            client.addHeader("X-AUTH-TOKEN", "0");
            client.get(
                    this,
                    "https://byta.ml/apiV2/login.php?email=" + email + "&password=" + password
                            + "&nombre=&apellidos=&ubicacion=",
                    new LoginHandler(this, email, password, "email", settings)
            );

        } else {
            Toast toastMailMalo = Toast.makeText(getApplicationContext(), getString(R.string.toastEmailMalo),Toast.LENGTH_LONG);
            toastMailMalo.show();
        }


    }
}
