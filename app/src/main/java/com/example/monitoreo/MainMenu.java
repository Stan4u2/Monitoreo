package com.example.monitoreo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
    private DrawerLayout drawerLayout;
    private APIService mAPIService;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

            switch (window) {
                case "objects":
                    System.out.println("sent object");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Objects()).commit();
                    navigationView.setCheckedItem(R.id.nav_elements);
                    break;
                case "areas":
                    break;
                case "sections":
                    break;
            }
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Home()).commit();
                navigationView.setCheckedItem(R.id.nav_home);
                window = "main";
            }
        }

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
            case R.id.nav_elements:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Objects()).commit();
                break;
            case R.id.nav_areas:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Areas()).commit();
                break;
            case R.id.nav_new_user:
                if (MainActivity.isAdmin) {
                    Intent intent = new Intent(getApplicationContext(), SignUp.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Permiso Denegado", Toast.LENGTH_LONG).show();
                }
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

    public void MainMenuButtons(String nav){
        if (nav.equals("elements")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Objects()).commit();
            navigationView.setCheckedItem(R.id.nav_elements);
        } else if (nav.equals("areas")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Areas()).commit();
            navigationView.setCheckedItem(R.id.nav_areas);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
