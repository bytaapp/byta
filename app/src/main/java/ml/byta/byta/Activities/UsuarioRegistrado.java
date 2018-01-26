package ml.byta.byta.Activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.SyncHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import link.fls.swipestack.SwipeStack;
import ml.byta.byta.Adapters.AdapterProductos;
import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Object;
import ml.byta.byta.EventListeners.ClickBotonesSwipe;
import ml.byta.byta.EventListeners.SuperlikeObjectClickListener;
import ml.byta.byta.EventListeners.SwipeStackCardListener;
import ml.byta.byta.Objects.Producto;
import ml.byta.byta.R;
import ml.byta.byta.REST.ClasePeticionRest;
import ml.byta.byta.Server.Handlers.ObjectsHandler;
import ml.byta.byta.Server.RequestsToServer;
import ml.byta.byta.Tools.Connectivity;
import pl.droidsonroids.gif.GifImageView;

public class UsuarioRegistrado extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RequestsToServer {

    URL imageUrl;
    TextView txt;
    CircleImageView img;

    private String token,email;          // Contiene el método que utilizó el usuario para registrarse.

    private DrawerLayout drawer;

    private PopupWindow popUpWindow;

    private TextView noMoreImages;
    private ImageView bytaLogoWallpaper;
    private GifImageView gifImageView;

    private AdapterProductos adapterProductos;

    public static ArrayList<Producto> productos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        productos.clear();
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_usuario_registrado);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        noMoreImages = (TextView) findViewById(R.id.no_imagenes);
        bytaLogoWallpaper = (ImageView) findViewById(R.id.byta_logo_wallpaper);
        gifImageView = (GifImageView) findViewById(R.id.gif_carga);

        // Se selecciona el menú lateral de navegación.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences settings = getSharedPreferences("Config", 0);
        token = FirebaseInstanceId.getInstance().getToken();
        email = settings.getString("email","");

        //Guardamos el token actual del usuario en la base de datos
        new ClasePeticionRest.GuardarToken(UsuarioRegistrado.this, token, email).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        // TODO: mejorar lo siguiente.

        // --------------------------------------------------------------------------------------

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                // Pila de cartas.
                SwipeStack swipeStack= (SwipeStack) findViewById(R.id.pila_cartas);

                // Se extraen los objetos de la base de datos.
                List<Object> objectsFromDB = Database.db.objectDao().getAllNotViewed();

                if (objectsFromDB != null) {    // Hay objetos en la base de datos.

                    productos = new ArrayList<>();

                    Producto producto;

                    for (int i = 0; i < objectsFromDB.size(); i++) {
                        producto = new Producto(
                                objectsFromDB.get(i).getDescription(),
                                objectsFromDB.get(i).getLocation(),
                                objectsFromDB.get(i).getServerId()
                        );

                        productos.add(producto);
                    }

                    adapterProductos = new AdapterProductos(UsuarioRegistrado.this, productos);

                    swipeStack.setAdapter(adapterProductos);
                    swipeStack.setListener(new SwipeStackCardListener(UsuarioRegistrado.this, productos));

                    final int count = objectsFromDB.size();

                    UsuarioRegistrado.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterProductos.notifyDataSetChanged();

                            if (count > 0) {
                                noMoreImages.setVisibility(View.GONE);
                                bytaLogoWallpaper.setVisibility(View.GONE);
                                gifImageView.setVisibility(View.GONE);
                            } else {
                                noMoreImages.setVisibility(View.VISIBLE);
                                bytaLogoWallpaper.setVisibility(View.VISIBLE);
                                gifImageView.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                } else {
                    if (Connectivity.isConnectedFast(UsuarioRegistrado.this)) {
                        getObjectsLogged();
                    }

                }


            }
        });


        // --------------------------------------------------------------------------------------

        // Se selecciona la cabecera del menú lateral de navegación.
        View headerView = navigationView.getHeaderView(0);

        // Se asigna la información del usuario que aparece en el menú lateral.
        txt = (TextView) headerView.findViewById(R.id.nameUser);
        txt.setText(settings.getString("name", "") + " " + settings.getString("surname", ""));
        txt = (TextView) headerView.findViewById(R.id.emailUser);
        txt.setText(settings.getString("email", ""));
        img = (CircleImageView) headerView.findViewById(R.id.imageUser);

        // Se comprueba el método de registro para tratar la imagen del usuario.
        if (settings.getString("method", "").equals("google")) {

            try {
                URL url = new URL(settings.getString("foto", ""));
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                img.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (settings.getString("method", "").equals("facebook")) {

            try {
                imageUrl = new URL("https://graph.facebook.com/" + settings.getString("facebookID", "")
                        + "/picture?type=large");
                Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                img.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (settings.getString("method", "").equals("email")) {

            try {
                imageUrl = new URL("https://www.viawater.nl/files/default-user.png");
                Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                img.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        findViewById(R.id.BotonX).setOnClickListener(new ClickBotonesSwipe(this, false));
        findViewById(R.id.BotonTick).setOnClickListener(new ClickBotonesSwipe(this, true));
        findViewById(R.id.Botonsuper).setOnClickListener(new ClickBotonesSwipe(this, true, 1));
        //findViewById(R.id.Botonsuper).setOnClickListener(new SuperlikeObjectClickListener(this, productos.get(0).getId()));

    }

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
        getMenuInflater().inflate(R.menu.usuario_registrado, menu);
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

    // Con este método se controlan los clicks en los items del menú lateral de navegación.
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_tutorial_registrado) {       // Click en el tutorial.

            Intent intent = new Intent(this, Tutorial.class);
            startActivity(intent);

        } else if (id == R.id.nav_close) {          // Click en cerrar sesión.

            SharedPreferences settings = getSharedPreferences("Config", 0);
            SharedPreferences.Editor editor = settings.edit();

            if (settings.getString("method", "").equals("facebook")) {
                // TODO: ¿Qué hace esto?
                LoginManager.getInstance().logOut();
            }

            // Se limpian todos los datos almacenados en SharedPreferences.
            editor.clear();
            editor.commit();

            // Se limpian las tablas de la base de datos local.
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Database.db.objectDao().deleteAllObjects();
                    Database.db.chatDao().deleteAllChats();
                    Database.db.messageDao().deleteAllMessages();
                    Database.db.superlikedObjectDao().deleteAllObjects();
                }
            });

            // Se carga la activity "UsuarioNoRegistrado".
            Intent intent = new Intent(this, UsuarioNoRegistrado.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_contact2) {
            // Click en contacto.

            LayoutInflater inflater = (LayoutInflater) UsuarioRegistrado.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup, (ViewGroup) findViewById(R.id.popup_element));

            popUpWindow = new PopupWindow(layout, 1000, 700, true);
            popUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
            cancelButton.setOnClickListener(cancel_button_click_listener);

        } else if (id == R.id.nav_help_us_2) {
            // Click en Envíanos tus comentarios.

            sendEmail();

        } else if (id == R.id.nav_mis_objetos) {
            Intent intent = new Intent(this, ListadoObjetos.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences settings = getSharedPreferences("Config", 0);
        SharedPreferences.Editor editor = settings.edit();

        if (settings.getString("restartFromRewind","").equals("yes")) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    getObjectsLogged();
                }
            });
        }

        editor.putString("restartFromRewind", "no");
        editor.commit();

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
            Toast.makeText(UsuarioRegistrado.this,
                    "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
        }
    }

    public void abrirCamara(View view) {

        Intent intent = new Intent(this, Camara.class);
        startActivity(intent);

    }

    public void openChat(View view) {

        Intent intent = new Intent(this, ChatListActivity.class);
        startActivity(intent);

    }

    public void mislikes(View view) {

        Intent intent = new Intent(this, MisLikes.class);
        startActivity(intent);

    }

    public void rewind(View view) {

        Intent intent = new Intent(this, Rewind.class);
        startActivity(intent);

    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public void getChats() {

    }

    @Override
    public void getLikedObject() {

    }

    @Override
    public void getObjectsLogged() {
        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Se han pedido objetos en UsuarioRegistrado");
        Log.d("Main", "-------------------------------------------------------------------");

        SharedPreferences settings = getSharedPreferences("Config", 0);

        // Se selecciona el último objeto almacenado por su timestamp.
        Object lastObjectInTime = Database.db.objectDao().getLastObjectInTime();

        // Se hace una petición síncrona porque ya se hace en un hilo independiente.
        SyncHttpClient client = new SyncHttpClient();

        String url = "";

        // Se comprueba si el objeto extraido de la BD no es null, es decir, si hay objetos almacenados.
        if (lastObjectInTime == null) {
            Log.d("Main", "-------------------------------------------------------------------");
            Log.d("Main", "lastObjectInTime es NULL");
            Log.d("Main", "-------------------------------------------------------------------");
            // Timestamp = 0.
            url = "https://byta.ml/apiV2/pedir_objetos.php?modo=registrado&timestamp=0&sessionID=" +
                    settings.getString("sessionID", "");
        } else {
            //url = "https://byta.ml/apiV2/pedir_objetos.php?modo=registrado&timestamp=" + lastObjectInTime.getTimestamp()
            //        + "&sessionID=" + settings.getString("sessionID", "");
            url = "https://byta.ml/apiV2/pedir_objetos.php?modo=registrado&timestamp=0&sessionID=" +
                    settings.getString("sessionID", "");

            Log.d("Main", "-------------------------------------------------------------------");
            Log.d("Main", "Descripción objeto más reciente --> " + lastObjectInTime.getDescription());
            Log.d("Main", "-------------------------------------------------------------------");
        }

        // Se hace la petición al servidor.
        client.get(
                this,
                url,
                new ObjectsHandler(this, true)
        );
    }

    @Override
    public void getMessages() {

    }
}