package com.example.monitoreo;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoreo.data.model.Section;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSection extends AppCompatActivity {

    ImageButton SaveSection, CancelSection;
    EditText NewSection;

    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_section);

        //Get the size of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        //With these three line below the Activity will be shown like a little square now using all the space available.
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .50), (int) (height * .30));

        mAPIService = APIUtils.getAPIService();

        //ImageButton
        SaveSection = findViewById(R.id.SaveSection);
        CancelSection = findViewById(R.id.CancelSection);

        //Edit Text
        NewSection = findViewById(R.id.NewSection);



        CancelSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void saveSection(View view) {
        if (!NewSection.getText().toString().isEmpty()) {
            Section section = new Section(NewSection.getText().toString());

            Call<Section> call = mAPIService.createSection(MainActivity.tokenAuth,section);

            call.enqueue(new Callback<Section>() {
                @Override
                public void onResponse(Call<Section> call, Response<Section> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Inserccion Seccion Exitoso", Toast.LENGTH_LONG).show();
                        finish();
                    } else if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Inserccion Seccion Fallido", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Section> call, Throwable t) {
                    Log.e("AddSection", "onFailure: " + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Campo Vacio", Toast.LENGTH_SHORT).show();
        }
    }
}
