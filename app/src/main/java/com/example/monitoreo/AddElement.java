package com.example.monitoreo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.model.Section;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import com.example.monitoreo.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddElement extends AppCompatActivity {

    ArrayList<String> areaList;
    ArrayList<Area> areasObjects;
    ArrayList<String> sectionList;
    ArrayList<Section> sectionObjects;
    private APIService mAPIService;
    private ImageButton AddNewArea, AddNewSection;
    private Spinner AreaSpinner, SectionSpinner;
    private EditText RFID, Label, Descriptor, Observations;
    private RadioButton ActiveRB, InactiveRB;

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

        //getAreas();
        //getSections();

        ActiveRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    InactiveRB.setChecked(false);
                }
            }
        });

        InactiveRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ActiveRB.setChecked(false);
                }
            }
        });

        AddNewArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(getApplicationContext(), AddArea.class);
                startActivity(intent);
            }
        });

        AddNewSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(getApplicationContext(), AddSection.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getAreas();
        getSections();
    }

    private void getSections() {
        sectionObjects = new ArrayList<Section>();

        Call<List<Section>> call = mAPIService.getAllSections(MainActivity.tokenAuth);

        call.enqueue(new Callback<List<Section>>() {
            @Override
            public void onResponse(Call<List<Section>> call, Response<List<Section>> response) {
                if (response.isSuccessful()) {
                    List<Section> sections = response.body();

                    for (Section section : sections) {
                        sectionObjects.add(section);
                    }

                    obtainList_Sections();

                    ArrayAdapter adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_item, sectionList);

                    SectionSpinner.setAdapter(adapter);
                } else {
                    Log.e("AddElement Section", "onFailure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Section>> call, Throwable t) {
                Log.e("AddElement Section", "onFailure: " + t.getMessage());
            }
        });
    }

    private void obtainList_Sections() {
        //Fill the array list with all the sections
        sectionList = new ArrayList<String>();
        sectionList.add("Seleccione");
        for (int i = 0; i < sectionObjects.size(); i++){
            sectionList.add(sectionObjects.get(i).getName());
        }
    }

    public void getAreas() {
        areasObjects = new ArrayList<Area>();

        Call<List<Area>> call = mAPIService.getAllAreas(MainActivity.tokenAuth);

        call.enqueue(new Callback<List<Area>>() {
            @Override
            public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {
                if (response.isSuccessful()) {
                    List<Area> areas = response.body();

                    for (Area area : areas) {
                        areasObjects.add(area);
                    }

                    obtainList_Areas();

                    ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, areaList);

                    AreaSpinner.setAdapter(adapter);
                } else {
                    Log.e("AddElement Area", "onFailure: " + response.message());
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Area>> call, Throwable t) {
                Log.e("AddElement Area", "onFailurre: " + t.getMessage());
            }
        });
    }

    private void obtainList_Areas() {
        //Fill the array list with all the areas
        areaList = new ArrayList<String>();
        areaList.add("Seleccione");
        for (int i = 0; i < areasObjects.size(); i++) {
            areaList.add(areasObjects.get(i).getName());
        }
    }
}
