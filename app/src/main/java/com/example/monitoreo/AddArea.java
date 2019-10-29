package com.example.monitoreo;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddArea extends AppCompatActivity {

    Bundle objectSent;
    static int idAreaModify;
    public static String action;
    Boolean StateArea;
    TextView action_to_do;
    ImageButton SaveArea, CancelArea;
    EditText NewArea;
    RadioButton AreaActiveRB, AreaInactiveRB;

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

        //TextView
        action_to_do = findViewById(R.id.action_to_do);

        //Image Button
        SaveArea = findViewById(R.id.SaveArea);
        CancelArea = findViewById(R.id.CancelArea);

        //Edit Text
        NewArea = findViewById(R.id.NewArea);

        //RadioButton
        AreaActiveRB = findViewById(R.id.AreaActiveRB);
        AreaInactiveRB = findViewById(R.id.AreaInactiveRB);

        CancelArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        objectSent = getIntent().getExtras();
        if (objectSent != null) {
            action = objectSent.getSerializable("action").toString();

            switch (action) {
                case "insert":
                    action_to_do.setText("Nueva Area");
                    AreaActiveRB.setVisibility(View.GONE);
                    AreaInactiveRB.setVisibility(View.GONE);
                    break;
                case "modify":
                    action_to_do.setText("Modificar Area");
                    loadData();
                    break;
            }
        }

        AreaActiveRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    StateArea = true;
                }
            }
        });

        AreaInactiveRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    StateArea = false;
                }
            }
        });
    }

    private void loadData() {
        Area area = null;

        if (objectSent != null){
            area = (Area) objectSent.getSerializable("area");

            idAreaModify = area.getId();
            NewArea.setText(area.getName());

            if(area.getState()){
                AreaActiveRB.setChecked(true);
            } else {
                AreaInactiveRB.setChecked(true);
            }
        }
    }

    public void checkAreaInputs(View view) {
        if (!NewArea.getText().toString().isEmpty()) {
            if (action.equals("insert")) {
                saveArea(NewArea.getText().toString(), true);
            } else if (action.equals("modify")){
                modifyArea(NewArea.getText().toString(), StateArea);
            }
            System.out.println(action);
        } else {
            Toast.makeText(getApplicationContext(), "Campo Vacio", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveArea(String name, Boolean state){
        Area area = new Area(name, state);

        Call<Area> call = mAPIService.createArea(MainActivity.tokenAuth, area);

        call.enqueue(new Callback<Area>() {
            @Override
            public void onResponse(Call<Area> call, Response<Area> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Inserccion Area Exitoso", Toast.LENGTH_LONG).show();
                    finish();
                } else if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Inserccion Area Fallido", Toast.LENGTH_LONG).show();
                    Log.e("AddArea", "onFailure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Area> call, Throwable t) {
                Log.e("AddArea", "onFailure: " + t.getMessage());
            }
        });
    }

    public void modifyArea(String name, Boolean state){
        System.out.println("yeah");
        Call<Area> call = mAPIService.updateArea(MainActivity.tokenAuth, idAreaModify, name, state);

        call.enqueue(new Callback<Area>() {
            @Override
            public void onResponse(Call<Area> call, Response<Area> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Modificación Area Exitosa", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Modificación Area Fallido", Toast.LENGTH_LONG).show();
                    Log.e("ModifyArea", "onFailure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Area> call, Throwable t) {
                Log.e("ModifyArea", "onFailure: " + t.getMessage());
            }
        });
    }
}
