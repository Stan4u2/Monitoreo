package com.example.monitoreo;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoreo.data.model.User;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    EditText nameEditText, emailEditText, userEditText, passwordEditText;
    Button signUpButton;
    RadioButton radioButtonAdmin, radioButtonNormal;
    boolean isAdmin;
    private APIService mAPIService;

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

        //RadioButtons
        radioButtonAdmin = findViewById(R.id.radioButtonAdmin);
        radioButtonNormal = findViewById(R.id.radioButtonNormal);

        mAPIService = APIUtils.getAPIService();

        radioButtonAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAdmin = true;
                }
            }
        });

        radioButtonNormal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAdmin = false;
                }
            }
        });
    }

    public void signUp(View view) {
        //Check if all the edit texts aren't empty
        if (nameEditText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese el nombre", Toast.LENGTH_SHORT).show();
            return;
        } else if (emailEditText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese el correo electronico", Toast.LENGTH_SHORT).show();
            return;
        } else if (userEditText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese el usuario", Toast.LENGTH_SHORT).show();
            return;
        } else if (passwordEditText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese la contraseña", Toast.LENGTH_SHORT).show();
            return;
        } else if (!radioButtonAdmin.isChecked() && !radioButtonNormal.isChecked()) {
            Toast.makeText(getApplicationContext(), "Seleccione el tipo de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!checkEmail(emailEditText.getText().toString())){
            Toast.makeText(getApplicationContext(), "Ingrese un correo valido", Toast.LENGTH_SHORT).show();
            return;
        }

        //Create a new object with the new user values
        User user = new User(nameEditText.getText().toString(), isAdmin, userEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString(), 0);

        //Call the APIService class
        Call<User> call = mAPIService.createUser(user);

        //Check for errors and if it was a successful operation
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Usuario Creado", Toast.LENGTH_LONG).show();
                    finish();
                } else if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Error al crear usuario", Toast.LENGTH_LONG).show();
                    Log.e("SignUp", "onFailure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("SignUp", "onFailure: " + t.getMessage());
            }
        });
    }

    public boolean checkEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
