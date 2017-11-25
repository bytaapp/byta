package ml.byta.byta.Adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import ml.byta.byta.Objects.Producto;
import ml.byta.byta.R;


public class AdaptadorProductos extends BaseAdapter {

    Activity activity;
    private List<Producto> productos;

    public AdaptadorProductos(Activity activity, List<Producto> productos) {
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

        // Se asigna la imagen correspondiente que aparece en la carta..
        image.setImageBitmap(producto.getBitmap());

        // Se asigna un listener a la imagen que muestra la información del producto.
        //image.setOnClickListener(new OpenProductInfoClickListener(activity, getItem(position)));

        return convertView;

    }


    // Estos dos métodos no se qué hacen. Es parte del trabajo de Fermín.
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
