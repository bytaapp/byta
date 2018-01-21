package ml.byta.byta.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import ml.byta.byta.R;
import ml.byta.byta.REST.ClasePeticionRest;

public class Camara extends AppCompatActivity {

    Activity activity = this;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private static final String TAG = "MyActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private ImageView mImageView;
    private int result_code;
    private boolean foto_hecha;
    private TextView limit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        foto_hecha = false;
        setContentView(R.layout.activity_camara);
        verifyStoragePermissions(this);

        CropImage.activity()
                .setCropMenuCropButtonTitle(getResources().getString(R.string.crop))
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(3,4)
                .setFixAspectRatio(true)
                .start(activity);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        result_code = resultCode;

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            foto_hecha = true;
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();

                try {
                    mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream out = null;
                    try {

                        System.out.println(resultUri.getPath());
                        out = new FileOutputStream(resultUri.getPath());
                        Bitmap b = Bitmap.createScaledBitmap(mImageBitmap, 540, 720, false);
                        b.compress(Bitmap.CompressFormat.JPEG, 40, out);
                        //out = new FileOutputStream(mCurrentPhotoPath);
                        //b.compress(Bitmap.CompressFormat.JPEG, 70, out);
                        ImageView imageTest = findViewById(R.id.image_test);
                        imageTest.setImageBitmap(b);

                        Button aceptar = findViewById(R.id.camera_accept);
                        aceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EditText descripcion = findViewById(R.id.camera_description);

                                File foto = new File(resultUri.getPath());
                                new ClasePeticionRest.GuardarObjeto(activity,getSharedPreferences("Config", 0).getInt("id", 0),descripcion.getText().toString(),foto).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }
                        });

                        Button cancelar = findViewById(R.id.camera_cancel);
                        cancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(activity,UsuarioRegistrado.class);
                                activity.startActivity(intent);
                                finish();
                            }
                        });

                        // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (foto_hecha && result_code == 0){
            activity.finish();
        }
    }

    private File createImageFile(Context context) {
        // Create an image file name

        //TextView texto = (TextView)findViewById(R.id.texto);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),"/Byta/");
        Boolean mkdirs=false;
        if (!storageDir.exists()) {
            mkdirs = storageDir.mkdirs();
        }
        //texto.setText(storageDir.getAbsolutePath()+" "+mkdirs);
        Toast toast = Toast.makeText(context, storageDir.getAbsolutePath(), Toast.LENGTH_SHORT);
        toast.show();
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  // prefix
                    ".jpg",         // suffix
                    storageDir     // directory
            );
        } catch (IOException e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Toast.makeText(context,errors.toString(),Toast.LENGTH_LONG).show();
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


}
