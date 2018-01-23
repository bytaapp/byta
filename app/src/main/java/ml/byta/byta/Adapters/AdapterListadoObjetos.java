package ml.byta.byta.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ml.byta.byta.EventListeners.BorrarObjeto;
import ml.byta.byta.Objects.Producto;
import ml.byta.byta.R;

/**
 * Created by ibai on 10/19/17.
 */

public class AdapterListadoObjetos extends BaseAdapter {

    List<Producto> productos;
    LayoutInflater inflater;
    Activity activity;

    public AdapterListadoObjetos(Activity activity, List<Producto> productos) {
        this.productos = productos;
        this.activity = activity;
        inflater = LayoutInflater.from(this.activity);
    }

    @Override
    public int getCount() {
        return productos.size();
    }

    @Override
    public Producto getItem(int i) {
        return productos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AuxProducto auxProducto;

        if (view == null) {
            view = inflater.inflate(R.layout.listado_objetos_list_item, viewGroup, false);
            auxProducto = new AuxProducto(view);
            view.setTag(auxProducto);
        } else {
            auxProducto = (AuxProducto) view.getTag();
        }


        Producto producto = getItem(i);

        auxProducto.descripcionProducto.setText(producto.getDescription());
        auxProducto.miniatura.setImageBitmap(Bitmap.createScaledBitmap(producto.getBitmap(),810,1080,false));
        auxProducto.delete.setOnClickListener(new BorrarObjeto(activity, producto));

        return view;
    }

    private class AuxProducto {
        TextView descripcionProducto;
        ImageView delete, miniatura;

        public AuxProducto(View view) {
            descripcionProducto = view.findViewById(R.id.DescripcionListadoObjetos);
            delete = view.findViewById(R.id.BotonBorrarObjeto);
            miniatura = view.findViewById(R.id.ImagenMiniaturaObjeto);
        }
    }
}
