package com.example.monitoreo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddElement extends AppCompatActivity {

    private APIService mAPIService;

    ImageButton AddNewArea, AddNewSection;
    Spinner AreaSpinner, SectionSpinner;
    EditText RFID, Label, Descriptor, Observations;
    RadioButton ActiveRB, InactiveRB;

    ArrayList<String> areaList;
    ArrayList<Area> areasObjects;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_element);
        //Image Button
        AddNewArea = findViewById(R.id.AddNewArea);
        AddNewSection = findViewById(R.id.AddNewSection);
        //Spinner
        AreaSpinner = findViewById(R.id.AreaSpinner);
        SectionSpinner = findViewById(R.id.SectionSpinner);
        //EditText
        RFID = findViewById(R.id.RFID);
        Label = findViewById(R.id.Label);
        Descriptor = findViewById(R.id.Descriptor);
        Observations = findViewById(R.id.Observations);
        //RadioButton
        ActiveRB = findViewById(R.id.ActiveRB);
        InactiveRB = findViewById(R.id.InactiveRB);

        mAPIService = APIUtils.getAPIService();

        getAreas();

        ActiveRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    InactiveRB.setChecked(false);
                }
            }
        });

        InactiveRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ActiveRB.setChecked(false);
                }
            }
        });
    }

    public void getAreas(){
        areasObjects = new ArrayList<Area>();

        Call<List<Area>> call = mAPIService.getAllAreas();

        call.enqueue(new Callback<List<Area>>() {
            @Override
            public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {
                if(response.isSuccessful()){
                    List<Area> areas = response.body();

                    for(Area area : areas){
                        areasObjects.add(area);
                    }

                    obtainList_Areas();

                    ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, areaList);

                    AreaSpinner.setAdapter(adapter);
                }else{
                    System.out.println("Error: " + response.code());
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Area>> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });
    }

    private void obtainList_Areas(){
        areaList = new ArrayList<String>();
        areaList.add("Seleccione");
        for(int i = 0; i < areasObjects.size(); i++){
            areaList.add(areasObjects.get(i).getName());
        }
    }
}
