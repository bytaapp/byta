package ml.byta.byta.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.flexbox.FlexboxLayout;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ml.byta.byta.DataBase.Database;
import ml.byta.byta.DataBase.Object;
import ml.byta.byta.DataBase.SuperlikedObject;
import ml.byta.byta.EventListeners.DeleteFromMisLikesListener;
import ml.byta.byta.EventListeners.DislikeObjectClickListener;
import ml.byta.byta.EventListeners.SuperlikeObjectClickListener;
import ml.byta.byta.R;

public class MisLikes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_likes);

        final Activity activity = this;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }
        final int width = size.x;
        final int height = size.y;



        /* En "likedObjects" están todos los objetos que te gustan.
         * Para mostrar la imagen de un objeto hay que usar el método "loadImage(serverId)" donde el
         * parámetro "serverId" es el ID del objeto en el servidor. El método devuelve un Bitmap.
         * Por lo tanto, para cada objeto se debería hacer:
         *
         *      Bitmap bitmap = loadImage(object.getServerId());
         */

        new AsyncTask<Void, Void, List<Object>>() {
            @Override
            protected List<Object> doInBackground(Void... params) {
                List<Object> likedObjects = Database.db.objectDao().getAllViewed();
                List<SuperlikedObject> superlikedObjects = Database.db.superlikedObjectDao().getAllObjects();

                List<Object> likedObjectsToShow = new ArrayList<>();

                if (likedObjects != null && superlikedObjects != null) {
                    for (int i = 0; i < likedObjects.size(); i++) {

                        int count = 0;

                        for (int j = 0; j < superlikedObjects.size(); j++) {
                            if (likedObjects.get(i).getServerId() == superlikedObjects.get(j).getServerId()) {
                                count++;
                            }
                        }

                        if (count == 0) {   // El objeto likedObjects.get(i) no tiene superlike.
                            likedObjectsToShow.add(likedObjects.get(i));
                        }

                    }
                }


                return likedObjectsToShow;
            }


            protected void onPostExecute(final List<Object> likedObjectsToShow) {

                //Log.d("liked", String.valueOf(likedObjects.size()));
                Log.d("liked", "Me han gustado " + likedObjectsToShow.size() + " objetos");

                FlexboxLayout flex = (FlexboxLayout) findViewById(R.id.likedimages);
                for(int x=0;x<likedObjectsToShow.size();x++) {
                    //Creamos un LinearLayout
                    LinearLayout linearLayout = new LinearLayout(activity);
                    //Y un imageview que irá dentro
                    ImageView image = new ImageView(MisLikes.this);
                    image.setImageBitmap(loadImage(likedObjectsToShow.get(x).getServerId()));

                    //También otro linearlayout que incluirá los botones
                    LinearLayout botones = new LinearLayout(MisLikes.this);
                    //Botones
                    ImageView superlike = new ImageView(MisLikes.this);
                    superlike.setImageResource(R.drawable.supericon);

                    ImageView white = new ImageView(MisLikes.this);
                    white.setImageResource(R.drawable.white);

                    ImageView dislike = new ImageView(MisLikes.this);
                    dislike.setImageResource(R.drawable.x_icon);

                    // Listener para los botones "dislike" y "superlike".
                    Object object = likedObjectsToShow.get(x);
                   // superlike.setOnClickListener(new SuperlikeObjectClickListener(object));
                    SharedPreferences settings = getSharedPreferences("Config", 0);
                    dislike.setOnClickListener(new DeleteFromMisLikesListener(object,activity,settings));

                    //Añadimos los botones
                    botones.addView(dislike,width/9,width/9);
                    botones.addView(white,width/20, width/20);
                    botones.addView(superlike,width/9,width/9);

                    botones.setPadding(3,3,3,3);


                    //Añadimos el imageview al linearLayout
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width/2,height/3);

                    botones.setGravity(Gravity.CENTER);
                    linearLayout.addView(image);
                    linearLayout.addView(botones);

                    image.setLayoutParams(lp);
                    image.setPadding(3,3,3,3);
                    image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    //linearLayout.setBackgroundColor(Color.BLUE);
                    flex.addView(linearLayout);

                }

            }
        }.execute();






    }

    public Bitmap loadImage(int id) {
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(new URL("https://byta.ml/api/img/miniaturas_objetos/" + id + ".jpg").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

}
