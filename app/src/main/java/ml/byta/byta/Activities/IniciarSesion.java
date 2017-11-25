package ml.byta.byta.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ml.byta.byta.R;
import ml.byta.byta.REST.ClasePeticionRest;

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


        EditText edit = (EditText)findViewById(R.id.editText);
        EditText edit2 = (EditText)findViewById(R.id.editText2);

        String email = edit.getText().toString();
        String pass = edit2.getText().toString();
        String metodo = "email";

        if (esMailValido(email)){
            new ClasePeticionRest.HacerLogin(IniciarSesion.this, metodo, email, pass).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            Toast toastMailMalo = Toast.makeText(getApplicationContext(), getString(R.string.toastEmailMalo),Toast.LENGTH_LONG);
            toastMailMalo.show();
        }


    }
}
