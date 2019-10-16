package com.example.monitoreo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.model.Element;
import com.example.monitoreo.data.model.Section;

public class ElementDetails extends AppCompatActivity {

    TextView areaTextView, sectionTextView, RFIDTextView, LableTextView, DescriptorTextView, StateTextView, ObservationsTextView;

    Element element = null;
    Area area = null;
    Section section = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element_details);

        //Text Views
        areaTextView = findViewById(R.id.areaTextView);
        sectionTextView = findViewById(R.id.sectionTextView);
        RFIDTextView = findViewById(R.id.RFIDTextView);
        LableTextView = findViewById(R.id.LableTextView);
        DescriptorTextView = findViewById(R.id.DescriptorTextView);
        StateTextView = findViewById(R.id.StateTextView);
        ObservationsTextView = findViewById(R.id.ObservationsTextView);

        Bundle objectSent = getIntent().getExtras();

        if (objectSent != null) {
            element = (Element) objectSent.getSerializable("element");
            area = (Area) objectSent.getSerializable("area");
            section = (Section) objectSent.getSerializable("section");

            areaTextView.setText(area.getName());
            sectionTextView.setText(section.getName());
            RFIDTextView.setText(element.getRFID());
            LableTextView.setText(element.getLable());
            DescriptorTextView.setText(element.getDescriptor());
            if(element.getState()){
                StateTextView.setText("Activo");
            }else{
                StateTextView.setText("Inactivo");
            }

            ObservationsTextView.setText(element.getObservations());
        }

    }

}
