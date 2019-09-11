package com.example.monitoreo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    EditText userEditText, passwordEditText;
    Button loginButton, signUpLettersButton;
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
        signUpLettersButton = findViewById(R.id.signUpLettersButton);

        mAPIService = APIUtils.getAPIService();

        //App will check if there is a token saved if there is it'll login automatically
        checkToken();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpLettersButton:
                Intent intent = null;
                intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                break;
            case R.id.loginButton:
                login();
                break;
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
        User user = new User("", userEditText.getText().toString(), "", passwordEditText.getText().toString(), -1);

        //Call the APIService Class
        Call<User> call = mAPIService.login(user);

        //Check answers and for errors
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    //Get token
                    String id = response.body().getId();
                    System.out.println("Token: " + id);
                    //Call method to save the tokeen
                    saveToken(id);
                } else {
                    Log.e("Login", "onFailure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Login", "onFailure: " + t.getMessage());
            }
        });

    }

    public void saveToken(String token) {
        if (!token.isEmpty()) {
            //Save the token in a xml with the name of token
            SharedPreferences preferences = getSharedPreferences("token", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("token", token);
            editor.commit();
        }
    }

    public void checkToken() {
        //Check if the token exists
        SharedPreferences preferences = getSharedPreferences("token", Context.MODE_PRIVATE);
        //Get the value of the token if there is no token the default value will be empty string
        String token = preferences.getString("token", "");

        //If the string isn't empty then it'll login
        if (!token.isEmpty()) {
            start();
        }
    }

    public void start() {
        Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
    }
}
