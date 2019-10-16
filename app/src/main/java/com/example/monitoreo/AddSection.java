package com.example.monitoreo;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.model.Section;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSection extends AppCompatActivity {

    ImageButton SaveSection, CancelSection;
    EditText NewSection;
    Spinner AreaSectionSpinner;

    ArrayList<Area> areasObjects;
    ArrayList<String> areaList;

    int idSelectedArea;

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
        getWindow().setLayout((int) (width * .50), (int) (height * .40));

        mAPIService = APIUtils.getAPIService();

        //ImageButton
        SaveSection = findViewById(R.id.SaveSection);
        CancelSection = findViewById(R.id.CancelSection);

        //Edit Text
        NewSection = findViewById(R.id.NewSection);

        //Spinner
        AreaSectionSpinner = findViewById(R.id.AreaSectionSpinner);

        CancelSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getAreas();
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

                    AreaSectionSpinner.setAdapter(adapter);

                    AreaSectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                idSelectedArea = areasObjects.get(position - 1).getId();
                                System.out.println("Area Selected " + idSelectedArea);
                            } else {
                                idSelectedArea = 0;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            idSelectedArea = 0;
                        }
                    });
                } else {
                    Log.e("AddElement Area", "onFailure: " + response.message());
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Area>> call, Throwable t) {
                Log.e("AddElement Area", "onFailure: " + t.getMessage());
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

    public void saveSection(View view) {
        if (!NewSection.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese Nombre Sección", Toast.LENGTH_SHORT).show();
            return;
        } else if (idSelectedArea == 0) {
            Toast.makeText(getApplicationContext(), "Seleccione Una Area", Toast.LENGTH_SHORT).show();
            return;
        }
        Section section = new Section(NewSection.getText().toString(), true, idSelectedArea);

        Call<Section> call = mAPIService.createSection(MainActivity.tokenAuth, section);

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

    }
}
