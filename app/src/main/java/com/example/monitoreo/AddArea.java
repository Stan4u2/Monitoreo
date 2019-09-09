package com.example.monitoreo;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddArea extends AppCompatActivity {

    ImageButton SaveArea, CancelArea;
    EditText NewArea;

    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_area);

        //Get the size of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        //With these three line below the Activity will be shown like a little square now using all the space available.
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .50), (int) (height * .30));

        mAPIService = APIUtils.getAPIService();

        //Image Button
        SaveArea = findViewById(R.id.SaveArea);
        CancelArea = findViewById(R.id.CancelArea);

        //Edit Text
        NewArea = findViewById(R.id.NewArea);

        CancelArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void saveArea(View view) {
        if (!NewArea.getText().toString().isEmpty()) {
            Area area = new Area(NewArea.getText().toString());

            Call<Area> call = mAPIService.createArea(area);

            call.enqueue(new Callback<Area>() {
                @Override
                public void onResponse(Call<Area> call, Response<Area> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Inserccion Area Exitoso", Toast.LENGTH_LONG).show();
                        finish();
                    }else if (!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Inserccion Area Fallido", Toast.LENGTH_LONG).show();
                        Log.e("AddArea", "onFailure: " +  response.message());
                    }
                }

                @Override
                public void onFailure(Call<Area> call, Throwable t) {
                    Log.e("AddArea", "onFailure: " + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Campo Vacio", Toast.LENGTH_SHORT).show();
        }
    }
}
