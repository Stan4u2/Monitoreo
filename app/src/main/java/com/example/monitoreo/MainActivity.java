package com.example.monitoreo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoreo.data.model.User;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static String tokenAuth;
    public static Integer userID;
    public static Boolean isAdmin;
    public static Boolean connectedInternet;
    EditText userEditText, passwordEditText;
    Button loginButton;
    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Edit text
        userEditText = findViewById(R.id.userEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        //Button
        loginButton = findViewById(R.id.loginButton);

        mAPIService = APIUtils.getAPIService();

        connectedInternet = checkInternetConnection();
        //App will check if yoou have internet connection
        if (connectedInternet){
            //App will check if there is a token saved if there is it'll login automatically
            checkToken();
        } else {
            Toast.makeText(getApplicationContext(), "Conectese a internet", Toast.LENGTH_LONG).show();
        }

    }

    public void onClick(View view) {
        if (connectedInternet) {
            login();
        } else {
            Toast.makeText(getApplicationContext(), "Conectese a internet", Toast.LENGTH_LONG).show();
        }
    }

    private void login() {
        //Check if the edit texts aren't empty
        if (userEditText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese el Usuario", Toast.LENGTH_SHORT).show();
            return;
        } else if (passwordEditText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese la Contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        //Make a new object with the user data
        final User user = new User("", null, userEditText.getText().toString(), "", passwordEditText.getText().toString(), -1);

        //Call the APIService Class
        Call<User> call = mAPIService.login(user);

        //Check answers and for errors
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    //Get token
                    tokenAuth = response.body().getId();
                    //Get user id
                    userID = response.body().getUserId();
                    isAdmin = response.body().getAdmin();
                    System.out.println("Id user: " + userID);
                    System.out.println("Token: " + tokenAuth);
                    //Call method to save the tokeen
                    saveToken(tokenAuth, userID, isAdmin);
                    start();
                } else {
                    Log.e("Login", "onFailure: " + response.message());
                    System.out.println("chi");
                    if (response.message().equals("Unauthorized")) {
                        Toast.makeText(getApplicationContext(), "Usuario o Contraseña Incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Login", "onFailure: " + t.getMessage());
                if (String.valueOf(t.getMessage()).equals("Failed to connect to /192.168.1.100:3000")){
                    Toast.makeText(getApplicationContext(), "Asegurese de estar conectado a la red.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void saveToken(String token, int idUser, boolean isAdmin) {
        if (!token.isEmpty()) {
            //Save the token in a xml with the name of token
            SharedPreferences preferences = getSharedPreferences("token", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("token", token);
            editor.putInt("idUser", idUser);
            editor.putBoolean("isAdmin", isAdmin);
            editor.commit();
        }
    }

    public void checkToken() {
        //Check if the token exists
        SharedPreferences preferences = getSharedPreferences("token", Context.MODE_PRIVATE);
        //Get the value of the token if there is no token the default value will be empty string
        String token = preferences.getString("token", "");
        int idUser = preferences.getInt("idUser", 0);
        boolean admin = preferences.getBoolean("isAdmin", false);

        //If the string isn't empty then it'll login
        if (!token.isEmpty() && idUser != 0) {
            tokenAuth = token;
            userID = idUser;
            isAdmin = admin;
            start();
        }
    }

    public void start() {
        Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(intent);
    }

    public Boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }
}
