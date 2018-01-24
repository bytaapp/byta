package ml.byta.byta.Adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import ml.byta.byta.Objects.Producto;
import ml.byta.byta.R;


public class AdapterProductos extends BaseAdapter {

    Activity activity;
    private List<Producto> productos;

    public AdapterProductos(Activity activity, List<Producto> productos) {
        this.activity = activity;
        this.productos = productos;
    }

    @Override
    public int getCount() {
        return this.productos.size();
    }

    @Override
    public Producto getItem(int position) {
        return this.productos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Producto producto = this.getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stack_item_product, null, false);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        // Se asigna la imagen correspondiente que aparece en la carta.
        // TODO: habrá que cambiar este adapter pero hace que cambien muchas cosas (Listeners).

        // TODO: resolver mostrar imágenes para no registrado.

        SharedPreferences settings = activity.getSharedPreferences("Config", 0);

        if (producto.getBitmap() == null) {

            image.setImageBitmap(Bitmap.createScaledBitmap(loadImage(producto.getId()),810,1080,false));

        } else {
            image.setImageBitmap(Bitmap.createScaledBitmap(producto.getBitmap(),810,1080,false));
        }

        TextView description = activity.findViewById(R.id.DescripcionCarta);

        try {
            if (this.getItem(position - 2) != null && !this.getItem(position-2).getDescription().equals(null)) {
                description.setText(this.getItem(position - 2).getDescription());
            }else if (this.getItem(position-2).getDescription().equals(null)) {
                description.setText("");
            }else if(position==this.getCount()-1){
                description.setText("");
            } else {
                description.setText(this.getItem(position).getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*
        if (!settings.getString("sessionID", "").equals("")) {  // Usuario registrado.
            if (producto.getBitmap() == null) {

                image.setImageBitmap(loadImage(producto.getId()));

            } else {
                image.setImageBitmap(producto.getBitmap());
            }

            TextView description = activity.findViewById(R.id.DescripcionCarta);
            description.setText(producto.getDescription());

        } else {    // Usuario no registrado.

            if (producto.getBitmap() == null) {

                image.setImageBitmap(loadImage(producto.getId()));

            } else {
                image.setImageBitmap(producto.getBitmap());
            }

            TextView description = activity.findViewById(R.id.DescripcionCarta);
            description.setText(producto.getDescription());

        }
        */

        return convertView;

    }




    public Bitmap loadImage(int id) {

        Bitmap bitmap = null;

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Está cargando la imagen con ID " + id);
        Log.d("Main", "-------------------------------------------------------------------");

        try {
            bitmap = BitmapFactory.decodeStream(new URL("https://byta.ml/api/img/fotos_objetos/" + id + ".jpg").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

}
