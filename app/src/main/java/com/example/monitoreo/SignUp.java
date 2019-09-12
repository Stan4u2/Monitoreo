package com.example.monitoreo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.monitoreo.data.model.User;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private APIService mAPIService;

    EditText nameEditText, emailEditText, userEditText, passwordEditText;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Edit Text
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        userEditText = findViewById(R.id.userEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        //Button
        signUpButton = findViewById(R.id.signUpButton);

        mAPIService = APIUtils.getAPIService();
    }

    public void signUp (View view){
        //Check if all the edit texts aren't empty
        if(nameEditText.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Ingrese el nombre", Toast.LENGTH_SHORT).show();
            return;
        }else if (emailEditText.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Ingrese el correo electronico", Toast.LENGTH_SHORT).show();
            return;
        }else if (userEditText.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Ingrese el usuario", Toast.LENGTH_SHORT).show();
            return;
        }else if (passwordEditText.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Ingrese la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        //Create a new object with the new user values
        User user = new User(nameEditText.getText().toString(), userEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString(), 0);

        //Call the APIService class
        Call<User> call = mAPIService.createUser(user);

        //Check for errors and if it was a successful operation
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Usuario Creado", Toast.LENGTH_LONG).show();
                    finish();
                }else if (!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Error al crear usuario", Toast.LENGTH_LONG).show();
                    Log.e("SignUp", "onFailure: " +  response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("SignUp", "onFailure: " + t.getMessage());
            }
        });
    }

}
