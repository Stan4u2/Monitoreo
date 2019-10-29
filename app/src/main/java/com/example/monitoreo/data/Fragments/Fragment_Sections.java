package com.example.monitoreo.data.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
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

import com.example.monitoreo.Adapter.Adapter_Sections;
import com.example.monitoreo.AddSection;
import com.example.monitoreo.MainActivity;
import com.example.monitoreo.R;
import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.model.Section;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class Fragment_Sections extends Fragment {
    private static ArrayList<Section> sections;
    private static ArrayList<Area> areas;
    RecyclerView SectionsRecyclerView;
    ImageButton AddSection;
    Section section1 = null;
    Area area1 = null;
    private APIService mAPIService;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sections, container, false);

        //RecyclerView
        SectionsRecyclerView = view.findViewById(R.id.SectionsRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        SectionsRecyclerView.setLayoutManager(llm);
        //Button
        AddSection = view.findViewById(R.id.AddSection);

        mAPIService = APIUtils.getAPIService();

        AddSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                Bundle bundle = new Bundle();

                intent = new Intent(view.getContext(), AddSection.class);
                bundle.putSerializable("action", "insert");
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }

    public void onResume(){
        super.onResume();
        LoadSections loadSections = new LoadSections();
        loadSections.execute();
    }

    private void loadList(){
        if (!sections.isEmpty() && !areas.isEmpty() && sections.size() == areas.size()){
            Adapter_Sections adapter_sections = new Adapter_Sections(sections, areas);

            SectionsRecyclerView.setAdapter(adapter_sections);
        }
    }

    public class LoadSections extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            sections = new ArrayList<Section>();

            Call<List<Section>> call = mAPIService.getAllSections(MainActivity.tokenAuth);

            try {
                Response<List<Section>> response = call.execute();
                if(response.isSuccessful()){
                    List<Section> sections1 = response.body();

                    for (Section section : sections1) {
                        sections.add(section);
                    }
                    return true;
                }else {
                    Log.e("FragmentSections Sections", "onFailure: " + response.message());
                    return false;
                }
            }catch (Exception e){
                Log.e("FragmentSections Sections", "onFailure: " + e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean){
                new LoadAreas().execute(sections);
            }
        }
    }

    public class LoadAreas extends AsyncTask<ArrayList<Section>, Void, Void> {

        ArrayList<Section> sections = new ArrayList<>();

        @Override
        protected Void doInBackground(ArrayList<Section>... arrayLists) {
            //Get the parameters value
            ArrayList<Section> sections = arrayLists[0];
            areas = new ArrayList<Area>();

            Call<List<Area>> call = mAPIService.getAllAreas(MainActivity.tokenAuth);

            try {
                Response<List<Area>> response = call.execute();
                if (response.isSuccessful()){
                    List<Area> areas1 = response.body();

                    for (int i = 0; i < sections.size(); i++){
                        for (Area area: areas1) {
                            if (sections.get(i).getAreaId() == area.getId()){
                                areas.add(area);
                            }
                        }
                    }
                }else {
                    Log.e("FragmentSections Area", "onFailure: " + response.message());
                }
            } catch (Exception e){
                Log.e("FragmentSections Area", "onFailure: " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadList();
        }
    }
}
