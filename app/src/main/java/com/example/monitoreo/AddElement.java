package com.example.monitoreo;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.monitoreo.data.model.Element;
import com.example.monitoreo.data.model.Section;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddElement extends AppCompatActivity {

    public static String action;
    ArrayList<String> areaList;
    ArrayList<Area> areasObjects;
    ArrayList<String> sectionList;
    ArrayList<Section> sectionObjects;
    int idSelectedSection, idSelectedArea;
    Boolean StateElement;
    int idElementModify;
    private APIService mAPIService;
    private ImageButton AddNewArea, AddNewSection, CancelElement;
    private Spinner AreaSpinner, SectionSpinner;
    private EditText RFID, Label, Descriptor, Observations;
    private TextView action_to_do_element;
    private RadioButton ActiveRB, InactiveRB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_element);

        //TextView
        action_to_do_element = findViewById(R.id.action_to_do_element);
        //Image Button
        AddNewArea = findViewById(R.id.AddNewArea);
        AddNewSection = findViewById(R.id.AddNewSection);
        CancelElement = findViewById(R.id.CancelElement);
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

        Bundle objectSent = getIntent().getExtras();

        if (objectSent != null) {
            action = objectSent.getSerializable("action").toString();

            switch (action) {
                case "insert":
                    action_to_do_element.setText("Nuevo Elemento");
                    break;
                case "modify":
                    action_to_do_element.setText("Modificar Elemento");
                    break;
            }
        }

        getAreas();
        getSections();

        ActiveRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    StateElement = true;
                    InactiveRB.setChecked(false);
                }
            }
        });

        InactiveRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    StateElement = false;
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

        CancelElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void loadData() {
        Area area = null;
        Section section = null;
        Element element = null;

        Bundle objectSent = getIntent().getExtras();

        if (objectSent != null) {
            element = (Element) objectSent.getSerializable("element");
            area = (Area) objectSent.getSerializable("area");
            section = (Section) objectSent.getSerializable("section");

            idElementModify = element.getId();

            System.out.println("Tamaño de spinner " + areasObjects.size());

            for (int i = 0; i < areasObjects.size(); i++) {
                if (areasObjects.get(i).getId().equals(area.getId())) {
                    AreaSpinner.setSelection(i + 1);
                }
            }

            for (int i = 0; i < sectionObjects.size(); i++) {
                if (sectionObjects.get(i).getId().equals(section.getId())) {
                    SectionSpinner.setSelection(i + 1);
                }
            }

            RFID.setText(element.getRFID());
            Label.setText(element.getLable());
            Descriptor.setText(element.getDescriptor());
            Observations.setText(element.getObservations());

            if (element.getState()) {
                ActiveRB.setChecked(true);
            } else {
                InactiveRB.setChecked(true);
            }
        }


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

                    SectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                idSelectedSection = sectionObjects.get(position - 1).getId();
                            } else {
                                idSelectedSection = 0;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    if(action == "modify") {
                        loadData();
                    }
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
        for (int i = 0; i < sectionObjects.size(); i++) {
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

                    AreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                idSelectedArea = areasObjects.get(position - 1).getId();
                            } else {
                                idSelectedArea = 0;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    if(action == "modify") {
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

    public void checkInputs(View view) {
        Boolean noBlankSpaces = true;
        if (AreaSpinner.getSelectedItemId() == 0 && SectionSpinner.getSelectedItemId() == 0 &&
                RFID.getText().toString().isEmpty() && Label.getText().toString().isEmpty() &&
                Descriptor.getText().toString().isEmpty() && !ActiveRB.isChecked() && !InactiveRB.isChecked() &&
                Observations.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "¡¡Campos Vacios!!", Toast.LENGTH_LONG).show();
            noBlankSpaces = false;
        } else {
            if (AreaSpinner.getSelectedItemId() == 0) {
                Toast.makeText(getApplicationContext(), "¡¡Seleccione una area!!", Toast.LENGTH_LONG).show();
                noBlankSpaces = false;
            }
            if (SectionSpinner.getSelectedItemId() == 0) {
                Toast.makeText(getApplicationContext(), "¡¡Seleccione una sección!!", Toast.LENGTH_LONG).show();
                noBlankSpaces = false;
            }
            if (RFID.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "¡¡Ingrese una RFID!!", Toast.LENGTH_LONG).show();
                noBlankSpaces = false;
            }
            if (Label.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "¡¡Ingrese una etiqueta!!", Toast.LENGTH_LONG).show();
                noBlankSpaces = false;
            }
            if (Descriptor.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "¡¡Ingrese un descriptor!!", Toast.LENGTH_LONG).show();
                noBlankSpaces = false;
            }
            if (!ActiveRB.isChecked() && !InactiveRB.isChecked()) {
                Toast.makeText(getApplicationContext(), "¡¡Seleccione un estado!!", Toast.LENGTH_LONG).show();
                noBlankSpaces = false;
            }
            if (Observations.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "¡¡Ingrese una observación!!", Toast.LENGTH_LONG).show();
                noBlankSpaces = false;
            }
        }

        if (noBlankSpaces) {
            saveElement(
                    idSelectedArea,
                    idSelectedSection,
                    RFID.getText().toString(),
                    Label.getText().toString(),
                    Descriptor.getText().toString(),
                    StateElement,
                    Observations.getText().toString()
            );
        }
    }

    private void saveElement(int Area, int Section, String rfid, String lable, String descriptor, Boolean state, String observations) {

        Element element = new Element(rfid, lable, descriptor, state, observations, Area, Section);

        Call<Element> call = mAPIService.createElement(MainActivity.tokenAuth, element);

        call.enqueue(new Callback<Element>() {
            @Override
            public void onResponse(Call<Element> call, Response<Element> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Inserccion Elemento Exitoso", Toast.LENGTH_LONG).show();
                    finish();
                } else if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Inserccion Elemento Fallido", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Element> call, Throwable t) {
                Log.e("AddElement", "onFailure: " + t.getMessage());
            }
        });

    }

}
