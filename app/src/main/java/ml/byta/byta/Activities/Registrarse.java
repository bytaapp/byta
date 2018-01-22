package ml.byta.byta.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

import ml.byta.byta.R;
import ml.byta.byta.REST.ClasePeticionRest;
import ml.byta.byta.Server.Handlers.SignInHandler;
import ml.byta.byta.Tools.GetLocation;

public class Registrarse extends AppCompatActivity {

    GetLocation ubicacion = new GetLocation();

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

        EditText nameEditText = (EditText)findViewById(R.id.editText);
        EditText surnameEditText = (EditText)findViewById(R.id.editText2);
        EditText emailEditText = (EditText)findViewById(R.id.editText3);
        EditText passwordEditText = (EditText)findViewById(R.id.editText4);
        EditText confirmPasswordEditText = (EditText)findViewById(R.id.editText5);

        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmedPassword = confirmPasswordEditText.getText().toString();
        String location = ubicacion.getCoords(this);

        if (location != null){
            if (esMailValido(email) && !password.equals("")) {
                if (password.equals(confirmedPassword)) {

                    // TODO: AQUÍ HACER PETICIÓN AL SERVIDOR PARA REGISTRAR EL USUARIO
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get(
                            this,
                            "https://byta.ml/apiV2/registrar.php?nombre=" + name + "&apellidos=" + surname
                                    + "&email=" + email + "&password=" + password + "&ubicacion=" + location,
                            new SignInHandler(this, name, surname, email, password, location)
                    );

                } else {
                    Toast toastPassMal = Toast.makeText(getApplicationContext(), getString(R.string.toastPassMal), Toast.LENGTH_LONG);
                    toastPassMal.show();
                }
            } else {
                if (password.equals("")) {
                    Toast toastMailMalo = Toast.makeText(getApplicationContext(), "El campo contraseña no puede estar vacío", Toast.LENGTH_LONG);
                    toastMailMalo.show();
                } else {
                    Toast toastMailMalo = Toast.makeText(getApplicationContext(), getString(R.string.toastEmailMalo), Toast.LENGTH_LONG);
                    toastMailMalo.show();
                }
            }
        }else{
            Toast.makeText(this, "Por favor, activa la ubicación", Toast.LENGTH_LONG).show();
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
        }

    }
}
