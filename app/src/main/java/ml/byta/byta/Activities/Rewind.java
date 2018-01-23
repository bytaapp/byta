package ml.byta.byta.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;

import ml.byta.byta.Adapters.PickerAdapter;
import ml.byta.byta.EventListeners.SendRewindClickListener;
import ml.byta.byta.R;
import ml.byta.byta.Server.Handlers.LoginHandler;
import travel.ithaka.android.horizontalpickerlib.PickerLayoutManager;

public class Rewind extends AppCompatActivity {

    final Handler handler = new Handler();
    Activity activity = this;
    private Button sendRewindButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewind);


        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);

        PickerLayoutManager pickerLayoutManager = new PickerLayoutManager(this, PickerLayoutManager.HORIZONTAL, false);
        pickerLayoutManager.setChangeAlpha(true);
        pickerLayoutManager.setScaleDownBy(0.99f);
        pickerLayoutManager.setScaleDownDistance(0.8f);

        PickerAdapter adapter = new PickerAdapter(this, getData(100), rv);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rv);
        rv.setLayoutManager(pickerLayoutManager);
        rv.setAdapter(adapter);

        pickerLayoutManager.setOnScrollStopListener(new PickerLayoutManager.onScrollStopListener() {
            @Override
            public void selectedView(View view) {
                sendRewindButton = (Button) findViewById(R.id.btnrewind);
                String num = ((TextView) view).getText().toString();
                sendRewindButton.setText("Recuperar " + num + " objetos");
                sendRewindButton.setOnClickListener(new SendRewindClickListener(Rewind.this, Integer.parseInt(num)));
                //Toast.makeText(Rewind.this, ("Selected value : "+((TextView) view).getText().toString()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public List<String> getData(int count) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            data.add(String.valueOf(i));
        }
        return data;
    }

}
