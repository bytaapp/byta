package ml.byta.byta.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ml.byta.byta.EventListeners.BorrarObjeto;
import ml.byta.byta.Objects.Producto;
import ml.byta.byta.R;
import ml.byta.byta.Server.ModelsFromServer.ObjectFromServer;

/**
 * Created by ibai on 10/19/17.
 */

public class AdapterListadoObjetos extends BaseAdapter {

    ArrayList<ObjectFromServer> productos;
    LayoutInflater inflater;
    Activity activity;

    public AdapterListadoObjetos(Activity activity, ArrayList<ObjectFromServer> productos) {
        this.productos = productos;
        this.activity = activity;
        inflater = LayoutInflater.from(this.activity);
    }

    @Override
    public int getCount() {
        return productos.size();
    }

    @Override
    public ObjectFromServer getItem(int i) {
        return productos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.listado_objetos_list_item, viewGroup, false);
        }

        // EL objeto ProductoFromServer lo pasamos a un producto
        Producto producto = new Producto(getItem(i).getDescripcion(), getItem(i).getUbicacion(), getItem(i).getId());
        producto.setBitmap(loadImage(getItem(i).getId()));

        TextView descripcionProducto = view.findViewById(R.id.DescripcionListadoObjetos);
        ImageView delete = view.findViewById(R.id.BotonBorrarObjeto);
        ImageView miniatura = view.findViewById(R.id.ImagenMiniaturaObjeto);


        descripcionProducto.setText(producto.getDescription());
        delete.setOnClickListener(new BorrarObjeto(activity, producto));
        miniatura.setImageBitmap(producto.getBitmap());

        return view;
    }

    public Bitmap loadImage(int id) {

        Bitmap bitmap = null;

        Log.d("Main", "-------------------------------------------------------------------");
        Log.d("Main", "Está cargando la imagen con ID " + id);
        Log.d("Main", "-------------------------------------------------------------------");

        try {
            bitmap = BitmapFactory.decodeStream(new URL("https://byta.ml/api/img/miniaturas_objetos/" + id + ".jpg").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

}
