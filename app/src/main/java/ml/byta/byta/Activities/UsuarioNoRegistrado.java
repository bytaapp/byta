package ml.byta.byta.Activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import link.fls.swipestack.SwipeStack;
import ml.byta.byta.EventListeners.ClickBotonesSwipe;
import ml.byta.byta.Objects.Producto;
import ml.byta.byta.R;
import ml.byta.byta.REST.ClasePeticionRest;
import ml.byta.byta.Server.Handlers.ObjectsHandler;
import ml.byta.byta.Tools.Connectivity;
import pl.droidsonroids.gif.GifImageView;

public class UsuarioNoRegistrado extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static ArrayList<Producto> productos = new ArrayList<>();
    private PopupWindow popUpWindow;
    GifImageView gif;
    ImageView img;
    TextView text;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        productos.clear();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.setTitle("Swappie");
        //toolbar.setTitleTextColor(Color.rgb(240,98,146));

        //GetLocation location = new GetLocation();
        //String coor=location.getCoords(this);


        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Ha entrado en la activity UsuarioNoRegistrado");
        Log.d("Main", "-------------------------------------------------------------------");

        // ----------------------  MENÚ LATERAL ----------------------//

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // ---------------------- CARGAMOS LAS IMÁGENES ----------------------//


        // Pila de cartas.
        SwipeStack swipeStack= (SwipeStack) findViewById(R.id.pila_cartas);

        /*if (Connectivity.isConnectedFast(this)) {

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(
                    this,
                    "https://byta.ml/apiV2/pedir_objetos.php?modo=aleatorio",
                    new ObjectsHandler(this, false, swipeStack)
            );
            text = (TextView) findViewById(R.id.no_internet);
            text.setVisibility(View.GONE);
            btn = (Button) findViewById(R.id.retrybtn);
            btn.setVisibility(View.GONE);

        } else {
            gif= (GifImageView) findViewById(R.id.gif30);
            gif.setVisibility(View.GONE);
            img = (ImageView) findViewById(R.id.img50);
            img.setImageResource(R.drawable.nointernetconnection);
        }*/



        if (Connectivity.isConnectedFast(this)){
            new ClasePeticionRest.CogerObjetosAleatoriosInicio(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            text= (TextView) this.findViewById(R.id.no_internet);
            text.setVisibility(View.GONE);
            btn = (Button)this.findViewById(R.id.retrybtn);
            btn.setVisibility(View.GONE);

        }else{
            gif= (GifImageView) this.findViewById(R.id.gif30);
            gif.setVisibility(View.GONE);
            img = (ImageView)  this.findViewById(R.id.img50);
            img.setImageResource(R.drawable.nointernetconnection);
        }



        findViewById(R.id.BotonX).setOnClickListener(new ClickBotonesSwipe(this, false));
        findViewById(R.id.BotonTick).setOnClickListener(new ClickBotonesSwipe(this, true));

    }

    // ----------------------  MÉTODOS PARA INTERACCIONAR CON EL MENÚ LATERAL ----------------------//

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_tutorial) {

            Intent intent = new Intent(this, Tutorial.class);
            startActivity(intent);



        }else if (id == R.id.nav_contact) {


            LayoutInflater inflater = (LayoutInflater) UsuarioNoRegistrado.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup, (ViewGroup) findViewById(R.id.popup_element));

            popUpWindow = new PopupWindow(layout, 1000, 700, true);
            popUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
            cancelButton.setOnClickListener(cancel_button_click_listener);

        }else if(id ==R.id.nav_help_us){

            sendEmail();


            // } else if (id == R.id.nav_gallery) {

            //} else if (id == R.id.nav_manage) {

            //} else if (id == R.id.nav_share) {

            // } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void registrarse(View v){

        if (Connectivity.isConnectedFast(this)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this, UsuarioNoRegistrado.class);
            startActivity(intent);
            finish();


        }


        //registro.setVisibility(View.VISIBLE);
        //imagenes.setVisibility(View.GONE);
    }


    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            popUpWindow.dismiss();
        }
    };

    protected void sendEmail() {

        String mailto = "mailto:bytaapp@gmail.com" +
                "?cc=" + "" +
                "&subject=" + Uri.encode(getString(R.string.asunto)) +
                "&body=" + Uri.encode(getString(R.string.cuerpo));

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));


        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(UsuarioNoRegistrado.this,
                    "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
        }
    }

    public void reload(View v){
        Intent intent = new Intent(this, UsuarioNoRegistrado.class);
        startActivity(intent);
        finish();
    }

}
