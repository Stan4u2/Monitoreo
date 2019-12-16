package com.example.monitoreo.data.Fragments;

import android.app.ProgressDialog;
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

import com.example.monitoreo.Adapter.Adapter_Objects;
import com.example.monitoreo.AddElement;
import com.example.monitoreo.ElementDetails;
import com.example.monitoreo.MainActivity;
import com.example.monitoreo.R;
import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.model.Element;
import com.example.monitoreo.data.model.Readings;
import com.example.monitoreo.data.model.ReadingsDetails;
import com.example.monitoreo.data.model.Section;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class Fragment_Objects extends Fragment {

    private static ArrayList<Area> areasObjects;
    private static ArrayList<Section> sectionObjects;
    private static ArrayList<Element> elements;
    private static ArrayList<Readings> readings;
    private static ArrayList<ReadingsDetails> readingsDetails;
    RecyclerView ElementsRecyclerView;
    ImageButton AddObject;
    String action;

    Element element1 = null;
    Area area1 = null;
    Section section1 = null;
    Readings reading1 = null;
    ReadingsDetails readingsDetail1 = null;

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

        Bundle objectSent = this.getArguments();

        if (objectSent != null) {
            action = objectSent.getSerializable("action").toString();
            reading1 = (Readings) objectSent.getSerializable("reading");

            AddObject.setVisibility(View.GONE);
            System.out.println("chi1");
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle objectSent = this.getArguments();

        if (objectSent != null) {
            action = objectSent.getSerializable("action").toString();
            reading1 = (Readings) objectSent.getSerializable("reading");

            AddObject.setVisibility(View.GONE);
            System.out.println("chi2");
        }

        if (action != null) {
            if (action.equals("readingsDetails")) {
                GetListReadingDetails getListReadingDetails = new GetListReadingDetails(reading1);
                getListReadingDetails.execute();
            }
        } else {
            LoadElements loadElements = new LoadElements();
            loadElements.execute();
        }
    }

    private void loadList() {
        System.out.println(elements.size());
        System.out.println(areasObjects.size());
        System.out.println(sectionObjects.size());
        if (!elements.isEmpty() && !areasObjects.isEmpty() && !sectionObjects.isEmpty()
                &&
                elements.size() == areasObjects.size() && areasObjects.size() == sectionObjects.size() && sectionObjects.size() == elements.size()) {


            Adapter_Objects adapter_objects = new Adapter_Objects(elements, areasObjects, sectionObjects, readingsDetails);

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
    }

    public class GetListReadingDetails extends AsyncTask<Readings, Void, Boolean> {
        private Readings newReadings;

        public GetListReadingDetails(Readings newReadings) {
            this.newReadings = newReadings;
        }

        @Override
        protected Boolean doInBackground(Readings... readings) {
            System.out.println("id REading " + newReadings.getIdReading());
            Call<List<ReadingsDetails>> call = mAPIService.getReadingsDetails(MainActivity.tokenAuth, newReadings.getIdReading());

            readingsDetails = new ArrayList<ReadingsDetails>();

            try {
                Response<List<ReadingsDetails>> response = call.execute();
                if (response.isSuccessful()) {
                    List<ReadingsDetails> readingsDetails1 = response.body();

                    for (ReadingsDetails readingsDetail : readingsDetails1) {
                        System.out.println("Reading Deatils " + readingsDetail.getElementID());
                        readingsDetails.add(readingsDetail);
                    }
                    return true;
                } else {
                    Log.e("FragmentObjects ReadingsDetails", "onFailure: " + response.message());
                    return false;
                }
            } catch (Exception e) {
                Log.e("FragmentObjects ReadingsDetails", "onFailure: " + e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                new LoadElementsReadings().execute(readingsDetails);
            }
        }
    }

    public class LoadElementsReadings extends AsyncTask<ArrayList<ReadingsDetails>, Void, Boolean> {
        ProgressDialog progDailog = new ProgressDialog(getContext());

        ArrayList<ReadingsDetails> readingsDetails = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog.setMessage("Cargando...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected Boolean doInBackground(ArrayList<ReadingsDetails>... arrayLists) {
            //Get the parameters value
            ArrayList<ReadingsDetails> readingsDetails = arrayLists[0];
            elements = new ArrayList<Element>();

            Call<List<Element>> call = mAPIService.getAllElements(MainActivity.tokenAuth);

            try {
                Response<List<Element>> response = call.execute();
                if (response.isSuccessful()) {
                    List<Element> elements1 = response.body();

                    for (int i = 0; i < readingsDetails.size(); i++) {
                        for (Element element : elements1) {
                            if (readingsDetails.get(i).getElementID() == element.getId()) {
                                elements.add(element);
                            }
                        }
                    }
                    return true;
                } else {
                    Log.e("FragmentObjects Elements", "onFailure: " + response.message());
                    return false;
                }

            } catch (Exception e) {
                Log.e("FragmentObjects Elements", "onFailure: " + e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                new LoadAreas().execute(elements);
                new LoadSections().execute(elements);
            }
            progDailog.dismiss();
        }
    }

    public class LoadElements extends AsyncTask<Void, Void, Boolean> {

        ProgressDialog progDailog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progDailog.setMessage("Cargando...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            elements = new ArrayList<Element>();

            Call<List<Element>> call = mAPIService.getAllElements(MainActivity.tokenAuth);

            try {
                Response<List<Element>> response = call.execute();
                if (response.isSuccessful()) {
                    List<Element> elements1 = response.body();

                    for (Element element : elements1) {
                        elements.add(element);
                    }
                    return true;
                } else {
                    Log.e("FragmentObjects Elements", "onFailure: " + response.message());
                    return false;
                }

            } catch (Exception e) {
                Log.e("FragmentObjects Elements", "onFailure: " + e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                new LoadAreas().execute(elements);
                new LoadSections().execute(elements);
            }
            progDailog.dismiss();
        }
    }

    public class LoadAreas extends AsyncTask<ArrayList<Element>, Void, Void> {
        ArrayList<Element> elements = new ArrayList<>();

        @Override
        protected Void doInBackground(ArrayList<Element>... arrayLists) {
            //Get the parameters value
            ArrayList<Element> elements = arrayLists[0];
            areasObjects = new ArrayList<Area>();

            Call<List<Area>> call = mAPIService.getAllAreas(MainActivity.tokenAuth);

            try {
                Response<List<Area>> response = call.execute();
                if (response.isSuccessful()) {
                    List<Area> areas1 = response.body();

                    for (int i = 0; i < elements.size(); i++) {
                        for (Area area : areas1) {
                            if (elements.get(i).getAreaID() == area.getId()) {
                                areasObjects.add(area);
                            }
                        }
                    }
                } else {
                    Log.e("FragmentObjects Area", "onFailure: " + response.message());
                }

            } catch (Exception e) {
                Log.e("FragmentObjects Area", "onFailure: " + e);
            }
            return null;
        }
    }

    public class LoadSections extends AsyncTask<ArrayList<Element>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList<Element>... arrayLists) {
            //Get the parameters value
            ArrayList<Element> elements = arrayLists[0];
            sectionObjects = new ArrayList<Section>();

            Call<List<Section>> call = mAPIService.getAllSections(MainActivity.tokenAuth);

            try {
                Response<List<Section>> response = call.execute();
                if (response.isSuccessful()) {
                    List<Section> sections1 = response.body();

                    for (int i = 0; i < elements.size(); i++) {
                        for (Section section : sections1) {
                            if (elements.get(i).getSectionID() == section.getId()) {
                                sectionObjects.add(section);
                            }
                        }
                    }
                } else {
                    Log.e("FragmentObjects Area", "onFailure: " + response.message());
                }

            } catch (Exception e) {
                Log.e("FragmentObjects Area", "onFailure: " + e);
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
