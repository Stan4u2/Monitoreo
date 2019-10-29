package com.example.monitoreo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
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

    public static String action;
    static int idSectionModify;
    Bundle objectSent;
    Boolean StateSection;
    ArrayList<Area> areasObjects;
    ArrayList<String> areaList;
    int idSelectedArea;
    private Boolean loadInfo;
    private TextView action_to_do;
    private ImageButton SaveSection, CancelSection;
    private EditText NewSection;
    private Spinner AreaSectionSpinner;
    private RadioButton SectionActiveRB, SectionInactiveRB;
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

        //TextView
        action_to_do = findViewById(R.id.action_to_do);
        //ImageButton
        SaveSection = findViewById(R.id.SaveSection);
        CancelSection = findViewById(R.id.CancelSection);

        //Edit Text
        NewSection = findViewById(R.id.NewSection);

        //Spinner
        AreaSectionSpinner = findViewById(R.id.AreaSectionSpinner);

        //RadioButton
        SectionActiveRB = findViewById(R.id.SectionActiveRB);
        SectionInactiveRB = findViewById(R.id.SectionInactiveRB);

        CancelSection.setOnClickListener(new View.OnClickListener() {
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
                    action_to_do.setText("Nueva Sección");
                    SectionActiveRB.setVisibility(View.GONE);
                    SectionInactiveRB.setVisibility(View.GONE);
                    break;
                case "modify":
                    action_to_do.setText("Modificar Sección");
                    loadInfo = true;
                    break;
            }
        }

        SectionActiveRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    StateSection = true;
                }
            }
        });

        SectionInactiveRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    StateSection = false;
                }
            }
        });

        getAreas();
    }

    public void loadData() {
        if (loadInfo) {
            Section section = null;
            Area area = null;

            if (objectSent != null) {
                section = (Section) objectSent.getSerializable("section");
                area = (Area) objectSent.getSerializable("area");

                idSectionModify = section.getId();

                for (int i = 0; i < areasObjects.size(); i++) {
                    if (areasObjects.get(i).getId().equals(area.getId())) {
                        AreaSectionSpinner.setSelection(i + 1);
                        loadInfo = false;
                    }
                }

                NewSection.setText(section.getName());

                if (section.getState()) {
                    SectionActiveRB.setChecked(true);
                } else {
                    SectionInactiveRB.setChecked(true);
                }
            }
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
                    if (action.equals("modify")) {
                        loadData();
                    }
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

    public void checkInputs (View view){
        if (NewSection.getText().toString().isEmpty() && idSelectedArea == 0){
            Toast.makeText(getApplicationContext(), "Campos Vacios", Toast.LENGTH_SHORT).show();
            return;
        } else if (NewSection.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese Nombre Sección", Toast.LENGTH_SHORT).show();
            return;
        } else if (idSelectedArea == 0) {
            Toast.makeText(getApplicationContext(), "Seleccione Una Area", Toast.LENGTH_SHORT).show();
            return;
        }

        if (action.equals("insert")){
            saveSection(
                    NewSection.getText().toString(),
                    idSelectedArea
            );
        } else if (action.equals("modify")){
            modifySection(
                    idSectionModify,
                    idSelectedArea,
                    NewSection.getText().toString(),
                    StateSection);
        }
    }

    public void saveSection(String SectionName, Integer idArea) {


        Section section = new Section(SectionName, true, idArea);

        Call<Section> call = mAPIService.createSection(MainActivity.tokenAuth, section);

        call.enqueue(new Callback<Section>() {
            @Override
            public void onResponse(Call<Section> call, Response<Section> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Inserccion Seccion Exitoso", Toast.LENGTH_LONG).show();

                    //Return the ID of the section created.
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("inserted", "section");
                    returnIntent.putExtra("idNewSection", response.body().getId());
                    setResult(Activity.RESULT_OK, returnIntent);
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

    private void modifySection(Integer idSection, Integer idArea, String nameSection, Boolean stateSection) {
        Call<Section> call = mAPIService.updateSection(
                MainActivity.tokenAuth,
                idSection,
                nameSection,
                stateSection,
                idArea
        );

        call.enqueue(new Callback<Section>() {
            @Override
            public void onResponse(Call<Section> call, Response<Section> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Modificación Sección Exitoso", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Modificación Elemento Fallido", Toast.LENGTH_LONG).show();
                    Log.e("ModifySection", "onFailure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Section> call, Throwable t) {
                Log.e("ModifySection", "onFailure: " + t.getMessage());
            }
        });
    }
}
