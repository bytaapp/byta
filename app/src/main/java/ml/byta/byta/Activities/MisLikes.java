package ml.byta.byta.Activities;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;

import ml.byta.byta.R;

public class MisLikes extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_likes);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }
        int width = size.x;
        int height = size.y;


        FlexboxLayout flex = (FlexboxLayout) findViewById(R.id.likedimages);
        for(int x=0;x<9;x++) {
            //Creamos un LinearLayout
            LinearLayout linearLayout = new LinearLayout(this);
            //Y un imageview que irá dentro
            ImageView image = new ImageView(MisLikes.this);
            image.setImageResource(R.drawable.byta);
            //También otro linearlayout que incluirá los botones
            LinearLayout botones = new LinearLayout(MisLikes.this);
                //Botones
                ImageView superlike = new ImageView(MisLikes.this);
                superlike.setImageResource(R.drawable.x_icon);
                ImageView dislike = new ImageView(MisLikes.this);
                dislike.setImageResource(R.drawable.supericon);
                //Añadimos los botones
                botones.addView(superlike,width/7,width/7);
                botones.addView(dislike,width/7,width/7);
            //Añadimos el imageview al linearLayout
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width/2,height/3);

            botones.setGravity(Gravity.CENTER);
            linearLayout.addView(image);
            linearLayout.addView(botones);

            image.setLayoutParams(lp);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //linearLayout.setBackgroundColor(Color.BLUE);
            flex.addView(linearLayout);

        }

    }
}
