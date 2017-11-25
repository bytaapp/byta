package ml.byta.byta.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import ml.byta.byta.R;

public class Invitado extends AppCompatActivity {

    URL imageUrl;
    //SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitado);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //prefs = getSharedPreferences("es.unavarra.tlm", Context.MODE_PRIVATE);
        //String name = prefs.getString("es.tlm.unavarra.nombre","");
        //String email= prefs.getString("es.unavarra.tlm.email","hola");
        //String id = prefs.getString("es.unavarra.tlm.id","");
        //String birthday = prefs.getString("es.unavarra.tlm.birthday","");

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String id = getIntent().getStringExtra("id");




        ImageView img = (ImageView) findViewById(R.id.imageView2);

            try {
                imageUrl = new URL("https://graph.facebook.com/"+id+"/picture?type=large");
                Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                img.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        TextView text = (TextView)findViewById(R.id.email_fb);
        text.setText("Bienvenido "+email);

        }



    public void logout(View view) {
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    }


