package com.example.monitoreo.data.Fragments;

import android.app.ProgressDialog;
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

import com.example.monitoreo.Adapter.Adapter_Users;
import com.example.monitoreo.MainActivity;
import com.example.monitoreo.R;
import com.example.monitoreo.SignUp;
import com.example.monitoreo.UserData;
import com.example.monitoreo.data.model.User;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Users extends Fragment {
    private static ArrayList<User> users;
    RecyclerView UsersRecyclerView;
    ImageButton AddUser;

    User user1 = null;

    private APIService mAPIService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_users, container, false);

        //RecyclerView
        UsersRecyclerView = view.findViewById(R.id.UsersRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        UsersRecyclerView.setLayoutManager(llm);
        //Button
        AddUser = view.findViewById(R.id.AddUser);

        mAPIService = APIUtils.getAPIService();

        AddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                Bundle bundle = new Bundle();

                intent = new Intent(view.getContext(), SignUp.class);
                bundle.putSerializable("action", "insert");
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUsers();
    }

    private void getUsers () {
        users = new ArrayList<User>();

        final ProgressDialog progDailog = new ProgressDialog(getContext());
        progDailog.setMessage("Cargando...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();

        Call<List<User>> call = mAPIService.getAllUsers(MainActivity.tokenAuth);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()){
                    List<User> users1 = response.body();

                    for (User user : users1){
                        users.add(user);

                        user1 = new User();
                        user1.setUserId(user.getUserId());
                        user1.setAdmin(user.getAdmin());
                        user1.setUsername(user.getUsername());
                        user1.setName(user.getName());
                        user1.setEmail(user.getEmail());
                    }

                    loadList();
                } else {
                    Log.e("FragmentUsers", "onFailure: " + response.message());
                    return;
                }
                progDailog.dismiss();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("FragmentUsers", "onFailure: " + t.getMessage());
            }
        });
    }

    private void loadList() {
        if (!users.isEmpty()){
            Adapter_Users adapter_users = new Adapter_Users(users);

            adapter_users.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User user = users.get(UsersRecyclerView.getChildAdapterPosition(view));

                    Intent intent = new Intent(view.getContext(), UserData.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            UsersRecyclerView.setAdapter(adapter_users);
        }
    }
}
