package com.example.monitoreo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.monitoreo.data.Fragments.Fragment_Areas;
import com.example.monitoreo.data.Fragments.Fragment_Home;
import com.example.monitoreo.data.Fragments.Fragment_Objects;
import com.example.monitoreo.data.Fragments.Fragment_Sections;
import com.example.monitoreo.data.Fragments.Fragment_Users;
import com.example.monitoreo.data.model.User;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String window;
    boolean process;
    Bundle objectSent;
    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private APIService mAPIService;

    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.userName);
        TextView userRole = headerView.findViewById(R.id.userRole);

        //If user is admin the option to add a new user will appear, if not it won't
        Menu nav_Menu = navigationView.getMenu();
        if (MainActivity.isAdmin) {
            nav_Menu.findItem(R.id.nav_users).setVisible(true);
            nav_Menu.findItem(R.id.nav_new_user).setVisible(true);
            userRole.setText("ADMINISTRADOR");
        } else {
            nav_Menu.findItem(R.id.nav_users).setVisible(false);
            nav_Menu.findItem(R.id.nav_new_user).setVisible(false);
            userRole.setText("NORMAL");
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        mAPIService = APIUtils.getAPIService();
        System.out.println("Inside the Class");

        objectSent = getIntent().getExtras();

        if (objectSent != null) {
            System.out.println("ObjectSent");
            window = objectSent.getSerializable("window").toString();
            System.out.println("Window to select " + window);
            MainMenuButtons(window);
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Home()).commit();
                navigationView.setCheckedItem(R.id.nav_home);
                window = "main";
            }
        }

        getUserData(MainActivity.userID);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Home()).commit();
                break;
            case R.id.nav_users:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Users()).commit();
                break;
            case R.id.nav_elements:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Objects()).commit();
                break;
            case R.id.nav_areas:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Areas()).commit();
                break;
            case R.id.nav_sections:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Sections()).commit();
                break;
            case R.id.nav_new_user:
                Intent intent1 = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent1);
                break;
            case R.id.nav_change_password:
                Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                logout();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout() {
        SharedPreferences preferences = getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Call<APIService> call = mAPIService.logOut(MainActivity.tokenAuth);

        call.enqueue(new Callback<APIService>() {
            @Override
            public void onResponse(Call<APIService> call, Response<APIService> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Cerro Sección", Toast.LENGTH_LONG).show();

                    Intent miIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(miIntent);
                } else {
                    Log.e("Logout", "onFailure: " + response.message());
                    Toast.makeText(getApplicationContext(), "Error al cerrar sección", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<APIService> call, Throwable t) {
                Log.e("Logout", "onFailure: " + t.getMessage());
            }
        });
    }

    public void MainMenuButtons(String nav) {
        if (nav.equals("users")){
            if (MainActivity.isAdmin) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Users()).commit();
                navigationView.setCheckedItem(R.id.nav_users);
            } else {
                Toast.makeText(getApplicationContext(), "Permisos Insuficientes", Toast.LENGTH_LONG).show();
            }
        } else if (nav.equals("elements")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Objects()).commit();
            navigationView.setCheckedItem(R.id.nav_elements);
        } else if (nav.equals("areas")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Areas()).commit();
            navigationView.setCheckedItem(R.id.nav_areas);
        } else if (nav.equals("sections")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Sections()).commit();
            navigationView.setCheckedItem(R.id.nav_sections);
        }
    }

    public void getUserData(int idUser) {
        Call<User> call = mAPIService.getUserData(MainActivity.tokenAuth, idUser);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    response.body();
                    userName.setText(response.body().getName());
                } else {
                    Log.e("Login", "onFailure: " + response.message());
                    return;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
