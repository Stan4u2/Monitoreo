package com.example.monitoreo.data.Fragments;

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

import com.example.monitoreo.Adapter.Adapter_Areas;
import com.example.monitoreo.MainActivity;
import com.example.monitoreo.R;
import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Areas extends Fragment {
    RecyclerView AreasRecyclerView;
    ImageButton AddArea;
    Area area1 = null;
    private ArrayList<Area> areas;
    private APIService mAPIService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_areas, container, false);

        //RecyclerView
        AreasRecyclerView = view.findViewById(R.id.AreasRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        AreasRecyclerView.setLayoutManager(llm);

        //Button
        AddArea = view.findViewById(R.id.AddArea);

        mAPIService = APIUtils.getAPIService();

        AddArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAreas();
    }

    private void getAreas() {
        areas = new ArrayList<Area>();

        Call<List<Area>> call = mAPIService.getAllAreas(MainActivity.tokenAuth);

        call.enqueue(new Callback<List<Area>>() {
            @Override
            public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {
                if (response.isSuccessful()) {
                    List<Area> areas1 = response.body();

                    for (Area area : areas1) {
                        areas.add(area);

                        area1 = new Area();
                        area1.setId(area.getId());
                        area1.setName(area.getName());
                        area1.setState(area.getState());

                        System.out.println(area.getName());

                    }

                    loadList();
                } else {
                    Log.e("FragmentAreas Areas", "onFailure: " + response.message());
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Area>> call, Throwable t) {
                Log.e("FragmentAreas Areas", "onFailure: " + t.getMessage());
            }
        });
    }

    private void loadList() {
        if (!areas.isEmpty()){
            Adapter_Areas adapter_areas = new Adapter_Areas(areas);

            adapter_areas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Area area = areas.get(AreasRecyclerView.getChildAdapterPosition(view));
                }
            });

            AreasRecyclerView.setAdapter(adapter_areas);
        }
    }
}
