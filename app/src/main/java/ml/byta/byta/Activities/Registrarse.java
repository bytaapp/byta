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
import ml.byta.byta.Tools.GetLocation;

public class Registrarse extends AppCompatActivity {

    GetLocation location = new GetLocation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
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

    public void clickRegistrarse(View view){

        EditText edit = (EditText)findViewById(R.id.editText);
        EditText edit2 = (EditText)findViewById(R.id.editText2);
        EditText edit3 = (EditText)findViewById(R.id.editText3);
        EditText edit4 = (EditText)findViewById(R.id.editText4);
        EditText edit5 = (EditText)findViewById(R.id.editText5);

        String nombre = edit.getText().toString();
        String apellidos = edit2.getText().toString();
        String email = edit3.getText().toString();
        String pass = edit4.getText().toString();
        String pass2 = edit5.getText().toString();
        String ubicacion = location.getCoords(this);
        String metodo = "email";

        if (ubicacion != null){
            if (esMailValido(email) && !pass.equals("")) {
                if (pass.equals(pass2)) {
                    new ClasePeticionRest.GuardarUsuario(Registrarse.this, nombre, apellidos, email, pass, ubicacion, metodo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    Toast toastPassMal = Toast.makeText(getApplicationContext(), getString(R.string.toastPassMal), Toast.LENGTH_LONG);
                    toastPassMal.show();
                }
            } else {
                if (pass.equals("")) {
                    Toast toastMailMalo = Toast.makeText(getApplicationContext(), "El campo contraseña no puede estar vacío", Toast.LENGTH_LONG);
                    toastMailMalo.show();
                } else {
                    Toast toastMailMalo = Toast.makeText(getApplicationContext(), getString(R.string.toastEmailMalo), Toast.LENGTH_LONG);
                    toastMailMalo.show();
                }
            }
        }

    }
}
