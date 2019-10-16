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

    RecyclerView ElementsRecyclerView;
    ImageButton AddObject;
    Element element1 = null;
    Area area1 = null;
    Section section1 = null;
    private ArrayList<Area> areasObjects;
    private ArrayList<Section> sectionObjects;
    private ArrayList<Element> elements;
    private APIService mAPIService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_objects, container, false);

        //RecyclerView
        ElementsRecyclerView = view.findViewById(R.id.ElementsRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ElementsRecyclerView.setLayoutManager(llm);
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
                miIntent.putExtras(bundle);
                view.getContext().startActivity(miIntent);
            }
        });

        //getElements();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getElements();
    }

    private void loadList() {

        Adapter_Objects adapter_objects = new Adapter_Objects(elements, areasObjects, sectionObjects);


        adapter_objects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Element element = elements.get(ElementsRecyclerView.getChildAdapterPosition(view));
                Area area = areasObjects.get(ElementsRecyclerView.getChildAdapterPosition(view));
                Section section = sectionObjects.get(ElementsRecyclerView.getChildAdapterPosition(view));

                System.out.println(section.getName());

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

    private void getElements() {

        elements = new ArrayList<Element>();

        Call<List<Element>> call = mAPIService.getAllElements(MainActivity.tokenAuth);

        call.enqueue(new Callback<List<Element>>() {
            @Override
            public void onResponse(Call<List<Element>> call, Response<List<Element>> response) {
                if (response.isSuccessful()) {
                    List<Element> elements1 = response.body();

                    for (Element element : elements1) {
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

                        getElementArea(element.getAreaID());
                        getElementSection(element.getSectionID());

                    }
                } else {
                    Log.e("FragmentObjects Elements", "onFailure: " + response.message());
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Element>> call, Throwable t) {

            }
        });
    }

    private void getElementArea(int idArea) {
        areasObjects = new ArrayList<Area>();

        Call<Area> call = mAPIService.getElementArea(MainActivity.tokenAuth, idArea);

        call.enqueue(new Callback<Area>() {
            @Override
            public void onResponse(Call<Area> call, Response<Area> response) {
                if (response.isSuccessful()) {
                    area1 = new Area();
                    area1.setId(response.body().getId());
                    area1.setName(response.body().getName());

                    areasObjects.add(area1);
                } else {
                    Log.e("FragmentObjects Area", "onFailure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Area> call, Throwable t) {
                Log.e("FragmentObjects Area", "onFailure: " + t.getMessage());
            }
        });
    }

    private void getElementSection(int idSection) {
        sectionObjects = new ArrayList<Section>();

        Call<Section> call = mAPIService.getElementSection(MainActivity.tokenAuth, idSection);

        call.enqueue(new Callback<Section>() {
            @Override
            public void onResponse(Call<Section> call, Response<Section> response) {
                if (response.isSuccessful()) {

                    section1 = new Section();
                    section1.setId(response.body().getId());
                    section1.setName(response.body().getName());

                    sectionObjects.add(section1);

                    loadList();
                } else {
                    Log.e("FragmentObjects Section", "onFailure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Section> call, Throwable t) {
                Log.e("FragmentObjects Section", "onFailure: " + t.getMessage());
            }
        });
    }


}
