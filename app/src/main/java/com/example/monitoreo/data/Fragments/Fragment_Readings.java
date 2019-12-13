package com.example.monitoreo.data.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoreo.Adapter.Adapter_Readings;
import com.example.monitoreo.MainActivity;
import com.example.monitoreo.R;
import com.example.monitoreo.data.model.CallModel;
import com.example.monitoreo.data.model.Readings;
import com.example.monitoreo.data.model.User;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Readings extends Fragment {

    private static ArrayList<Readings> readings;
    private static ArrayList<User> users;

    RecyclerView ReadingsRecyclerView;
    ImageButton NewReading;

    Readings reading1 = null;
    User user1 = null;

    private APIService mAPIService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_readings, container, false);

        //RecyclerView
        ReadingsRecyclerView = view.findViewById(R.id.ReadingsRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ReadingsRecyclerView.setLayoutManager(llm);
        //Button
        NewReading = view.findViewById(R.id.NewReading);

        mAPIService = APIUtils.getAPIService();

        NewReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeNewReading();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadReadings loadReadings = new LoadReadings();
        loadReadings.execute();
    }

    public void makeNewReading() {

        Call<CallModel> call = mAPIService.makeNewReading(MainActivity.tokenAuth);

        call.enqueue(new Callback<CallModel>() {
            @Override
            public void onResponse(Call<CallModel> call, Response<CallModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Nueva Lectura Generada", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Ha fallado la lectura", Toast.LENGTH_LONG).show();
                    Log.e("GetNewReading", "onFailure: " + response.message());
                }
                LoadReadings loadReadings = new LoadReadings();
                loadReadings.execute();
            }

            @Override
            public void onFailure(Call<CallModel> call, Throwable t) {
                Log.e("GetNewReading", "onFailure: " + t.getMessage());
            }
        });
    }

    private void loadList() {
        if (!readings.isEmpty() && !users.isEmpty() && readings.size() == users.size()) {
            Adapter_Readings adapter_readings = new Adapter_Readings(readings, users);

            ReadingsRecyclerView.setAdapter(adapter_readings);
        }
    }

    public class LoadReadings extends AsyncTask<Void, Void, Boolean> {

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

            readings = new ArrayList<Readings>();

            Call<List<Readings>> call = mAPIService.getAllReadings(MainActivity.tokenAuth);

            try {
                Response<List<Readings>> response = call.execute();
                if (response.isSuccessful()) {
                    List<Readings> readings1 = response.body();

                    for (Readings reading : readings1) {
                        readings.add(reading);
                    }
                    return true;
                } else {
                    Log.e("FragmentSections Sections", "onFailure: " + response.message());
                    return false;
                }
            } catch (Exception e) {
                Log.e("FragmentReadings Readings", "onFailure: " + e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                new LoadUsers().execute(readings);
            }
            progDailog.dismiss();
        }
    }

    public class LoadUsers extends AsyncTask<ArrayList<Readings>, Void, Void> {

        ArrayList<Readings> readings = new ArrayList<>();

        @Override
        protected Void doInBackground(ArrayList<Readings>... arrayLists) {
            //Get the parameters value
            ArrayList<Readings> readings = arrayLists[0];
            users = new ArrayList<User>();

            Call<List<User>> call = mAPIService.getAllUsers(MainActivity.tokenAuth);

            try {
                Response<List<User>> response = call.execute();

                if (response.isSuccessful()) {
                    List<User> users1 = response.body();
                    for (int i = 0; i < readings.size(); i++) {
                        for (User user : users1) {
                            if (readings.get(i).getUserIDFK() == Integer.valueOf(user.getId())) {
                                users.add(user);
                            }
                        }
                    }
                } else {
                    Log.e("FragmentReadings Users", "onFailure: " + response.message());
                }
            } catch (Exception e) {
                Log.e("FragmentReadings Users", "onFailure: " + e);
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
