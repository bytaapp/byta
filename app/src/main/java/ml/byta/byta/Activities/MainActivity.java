package ml.byta.byta.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import ml.byta.byta.R;
import ml.byta.byta.REST.ClasePeticionRest;
import ml.byta.byta.Tools.GetLocation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private RelativeLayout sesion;
    private RelativeLayout nosesion;


    private  SignInButton google;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG="MAINACTIVITY";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private ProgressDialog progressDialog;

    String name = "";
    String email = "";
    String id = "";
    String birthday ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        verifyStoragePermissions(this);
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        // ----------------------  COMPROBAMOS SI YA HAY UNA SESIÓN INICIADA, Y SI LA HAY
        //                          INICIAMOS LA ACTIITY UsuarioRegistrado ----------------------//

        SharedPreferences settings = getSharedPreferences("Config", 0);
        boolean sesion = settings.getBoolean("sesion",false);
        if(sesion==true){

            Intent intent = new Intent(this, UsuarioRegistrado.class);
            startActivity(intent);
            finish();
        }

        //SHA1: FB:57:28:44:5A:27:FD:7F:77:05:F5:02:23:19:91:51:C7:30:FB:8A
        byte[] sha1 = {
                (byte) 0xFB, 0x57, 0x28, 0x44, 0x5A, 0x27, (byte) 0xFD, 0x7F, 0x77, 0x05, (byte) 0xF5, 0x02, 0x23, 0x19, (byte) 0x91, 0x51, (byte)0xC7, (byte)0x30, (byte)0xFB, (byte)0x8A
        };
        Log.d("keyhash", Base64.encodeToString(sha1, Base64.NO_WRAP));

        // ----------------------  LOGIN CON GOOGLE ----------------------//

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


       // google = (SignInButton) findViewById(R.id.loginButtonGoogle);

        findViewById(R.id.loginButtonGoogle).setOnClickListener(this);



        // ----------------------  LOGIN CON FACEBOOK ----------------------//

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email", "user_birthday"));


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {


                Log.d("tokenfacebook", String.valueOf(loginResult.getAccessToken()));



                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Log.d("tokenfacebook", response.toString());

                        displayUserInfo(object);

                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email,picture");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void signIn() {

        progressDialog= new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Signing in...");
        progressDialog.show();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, UsuarioNoRegistrado.class);
        startActivity(intent);
    }

    public void registro(View view){

        Intent intent = new Intent(this, Registrarse.class);
        startActivity(intent);
        finish();

    }

    public void loginEmail(View view){

        Intent intent = new Intent(this, IniciarSesion.class);
        startActivity(intent);
        finish();

    }

    public void logout(View view) {
        LoginManager.getInstance().logOut();
        SharedPreferences info = getSharedPreferences("Config", 0);
        SharedPreferences.Editor editor = info.edit();
        editor.putBoolean("sesion", false);
        sesion.setVisibility(View.GONE);
        nosesion.setVisibility(View.VISIBLE);

    }

    public void displayUserInfo(JSONObject object){


        String name ="";
        String email = "";
        String id="";

        try {
            name = object.getString("name");
            String[] name2 = name.split(" ");
            String first_name = name2[0];
            String last_name = name2[1];
            if (name2.length==3){
                last_name=last_name+" "+name2[2];
            }
            email = object.getString("email");
            id = object.getString("id");

            SharedPreferences info = getSharedPreferences("Config", 0);
            SharedPreferences.Editor editor = info.edit();
            editor.putString("metodo","facebook");
            editor.putBoolean("sesion", true);

            editor.putString("nombre",first_name);
            editor.putString("apellidos",last_name);

            editor.putString("idFacebook",id);
            editor.putString("email",email);

            editor.commit();

            Log.d("variables","HE INICIADO LA SESIÓN CON FACEBOOK");


        } catch (JSONException e) {
            e.printStackTrace();
        }



        new ClasePeticionRest.ComprobarFacebook(MainActivity.this,email).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //prefs = getSharedPreferences("es.unavarra.tlm", Context.MODE_PRIVATE);
        //prefs.edit().putString("es.unavarra.tlm.sesion", "true").apply();
        //prefs.edit().putString("es.unavarra.tlm.email", email).apply();
        //prefs.edit().putString("es.unavarra.tlm.id", id).apply();
        //prefs.edit().putString("es.unavarra.tlm.birthday", birthday).apply();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButtonGoogle:
                signIn();
                break;
            // ...
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();

            Log.d("tokengoogle",account.getIdToken());
           // Log.d("tokengoogle",account.getServerAuthCode());
            String name = account.getDisplayName();
            String[] name2 = name.split(" ");
            String first_name = name2[0];
            String last_name = name2[1];
            if (name2.length==3){
                last_name=last_name+" "+name2[2];
            }
            String imagen = String.valueOf(account.getPhotoUrl());
            String id = account.getIdToken();
            String email = account.getEmail();
            //String ubicacion = GetLocation.getCoords(this);

            new ClasePeticionRest.ComprobarGoogle(MainActivity.this, first_name, last_name, email, imagen).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            /*
            if (ubicacion != null) {

                SharedPreferences info = getSharedPreferences("Config", 0);
                SharedPreferences.Editor editor = info.edit();
                editor.putString("metodo", "google");
                editor.putString("nombre", first_name);
                editor.putString("apellidos", last_name);
                editor.putString("email", email);
                editor.putBoolean("sesion", true);
                editor.putString("foto", imagen);
                editor.commit();
            }
            */

            progressDialog.dismiss();

        } else {
            // Signed out, show unauthenticated UI.

        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
