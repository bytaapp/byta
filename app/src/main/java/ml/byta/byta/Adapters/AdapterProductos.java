package ml.byta.byta.Adapters;

import android.app.Activity;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;

import ml.byta.byta.Objects.Producto;
import ml.byta.byta.R;


public class AdapterProductos extends BaseAdapter implements Runnable {

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        Producto producto = this.getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stack_item_product, null, false);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        // Se asigna la imagen correspondiente que aparece en la carta.
        // TODO: habrá que cambiar este adapter pero hace que cambien muchas cosas (Listeners).

        if (producto.getBitmap() == null) {
            image.setImageBitmap(loadImage(producto.getId()));
            Log.d("Main", "-------------------------------------------------------------------");
            Log.d("Main", "Está mostrando la imagen con descripción " + producto.getDescription());
            Log.d("Main", "-------------------------------------------------------------------");
        } else {
            image.setImageBitmap(producto.getBitmap());
        }

        return convertView;

    }


    public Bitmap loadImage(int id) {
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(new URL("https://byta.ml/api/img/fotos_objetos/" + id + ".jpg").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    public void run() {

    }
}
