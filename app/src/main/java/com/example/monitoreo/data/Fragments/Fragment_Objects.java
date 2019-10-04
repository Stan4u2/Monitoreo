package com.example.monitoreo.data.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoreo.Adapter.Adapter_Objects;
import com.example.monitoreo.AddElement;
import com.example.monitoreo.ElementDetails;
import com.example.monitoreo.MainActivity;
import com.example.monitoreo.R;
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

public class Fragment_Objects extends Fragment {

    private ArrayList<Area> areasObjects;
    private ArrayList<Section> sectionObjects;
    private ArrayList<Element> elements;

    private APIService mAPIService;

    RecyclerView ElementsRecyclerView;

    ImageButton AddObject;

    Element element1 = null;
    Area area1 = null;
    Section section1 = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_objects, container, false);

        //RecyclerView
        ElementsRecyclerView = view.findViewById(R.id.ElementsRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ElementsRecyclerView.setLayoutManager(llm);
        //list.setAdapter( adapter );
        //Button
        AddObject = view.findViewById(R.id.AddObject);

        mAPIService = APIUtils.getAPIService();

        AddObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent miIntent = null;
                Bundle bundle = new Bundle();

                miIntent = new Intent(view.getContext(), AddElement.class);
                bundle.putSerializable("action", "insert");

                if (miIntent != null) {
                    miIntent.putExtras(bundle);
                    view.getContext().startActivity(miIntent);
                }
            }
        });

        getElements();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getElements();
    }

    private void loadList() {
        System.out.println("Size Elements: " + elements.size());
        System.out.println("Size Area: " + areasObjects.size());
        System.out.println("Size Section: " + sectionObjects.size());

        Adapter_Objects adapter_objects = new Adapter_Objects(elements, areasObjects, sectionObjects);

        adapter_objects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Element element = elements.get(ElementsRecyclerView.getChildAdapterPosition(view));
                Area area = areasObjects.get(ElementsRecyclerView.getChildAdapterPosition(view));
                Section section = sectionObjects.get(ElementsRecyclerView.getChildAdapterPosition(view));

                Intent intent = new Intent(view.getContext(), ElementDetails.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("element", element);
                bundle.putSerializable("area", area);
                bundle.putSerializable("section", section);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        ElementsRecyclerView.setAdapter(adapter_objects);
    }

    private void getElements (){

        elements = new ArrayList<Element>();

        Call<List<Element>> call = mAPIService.getAllElements(MainActivity.tokenAuth);

        call.enqueue(new Callback<List<Element>>() {
            @Override
            public void onResponse(Call<List<Element>> call, Response<List<Element>> response) {
                if (response.isSuccessful()) {
                    List<Element> elements1 = response.body();

                    for(Element element : elements1){
                        elements.add(element);

                        element1 = new Element();
                        element1.setId(element.getId());
                        element1.setAreaID(element.getAreaID());
                        element1.setSectionID(element.getSectionID());
                        element1.setRFID(element.getRFID());
                        element1.setLable(element.getLable());
                        element1.setDescriptor(element.getDescriptor());
                        element1.setState(element.getState());
                        element1.setObservations(element.getObservations());

                    }

                    getAreas();
                }else{
                    Log.e("FragmentObjects Elements", "onFailure: " + response.message());
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Element>> call, Throwable t) {

            }
        });
    }

    private void getAreas() {
        areasObjects = new ArrayList<Area>();

        Call<List<Area>> call = mAPIService.getAllAreas(MainActivity.tokenAuth);

        call.enqueue(new Callback<List<Area>>() {
            @Override
            public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {
                if (response.isSuccessful()) {
                    List<Area> areas = response.body();

                    for (Area area : areas) {
                        areasObjects.add(area);

                        area1 = new Area();
                        area1.setId(area.getId());
                        area1.setName(area.getName());
                    }

                    getSections();

                } else {
                    Log.e("FragmentObjects Area", "onFailure: " + response.message());
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Area>> call, Throwable t) {
                Log.e("FragmentObjects Area", "onFailure: " + t.getMessage());
            }
        });
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

                        section1 = new Section();
                        section1.setId(section.getId());
                        section1.setName(section.getName());
                    }
                    loadList();

                } else {
                    Log.e("FragmentObjects Section", "onFailure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Section>> call, Throwable t) {
                Log.e("FragmentObjects Section", "onFailure: " + t.getMessage());
            }
        });
    }


}
