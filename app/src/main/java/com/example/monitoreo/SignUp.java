package com.example.monitoreo;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoreo.data.model.User;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    public static String action;
    public static int idUserModify;
    EditText nameEditText, emailEditText, userEditText, passwordEditText;
    TextView ActionUser;
    Button signUpButton;
    RadioButton radioButtonAdmin, radioButtonNormal;
    CheckBox checkboxChangePassword;
    boolean isAdmin;
    Bundle objectSent;
    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //TextView
        ActionUser = findViewById(R.id.ActionUser);

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

        //CheckBox
        checkboxChangePassword = findViewById(R.id.checkboxChangePassword);

        mAPIService = APIUtils.getAPIService();

        objectSent = getIntent().getExtras();
        if (objectSent != null) {
            action = objectSent.getSerializable("action").toString();

            switch (action) {
                case "insert":
                    ActionUser.setText("CREAR NUEVO USUARIO");
                    checkboxChangePassword.setVisibility(View.GONE);
                    break;
                case "modify":
                    ActionUser.setText("MODIFICAR USUARIO");
                    checkboxChangePassword.setVisibility(View.VISIBLE);
                    loadData();
                    break;
            }
        }

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

    private void loadData() {
        User user = null;

        if (objectSent != null) {
            user = (User) objectSent.getSerializable("user");

            idUserModify = Integer.valueOf(user.getId());

            nameEditText.setText(user.getName());
            emailEditText.setText(user.getEmail());
            userEditText.setText(user.getUsername());

            if (user.getAdmin()) {
                radioButtonAdmin.setChecked(true);
            } else {
                radioButtonNormal.setChecked(true);
            }
        }
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
        } else if (!radioButtonAdmin.isChecked() && !radioButtonNormal.isChecked()) {
            Toast.makeText(getApplicationContext(), "Seleccione el tipo de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        if (action.equals("insert")) {
            if (passwordEditText.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Ingrese la contraseña", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!checkEmail(emailEditText.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Ingrese un correo valido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (action.equals("insert")) {
            insertNewUser(
                    nameEditText.getText().toString(),
                    isAdmin,
                    userEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    passwordEditText.getText().toString()
            );
        } else if (action.equals("modify")) {
            System.out.println("It's going to modify the user");
            if (checkboxChangePassword.isChecked()) {
                modifyUserWithPassword(
                        idUserModify,
                        nameEditText.getText().toString(),
                        isAdmin,
                        userEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString()
                );
            } else {
                modifyUserNoPassword(
                        idUserModify,
                        nameEditText.getText().toString(),
                        isAdmin,
                        userEditText.getText().toString(),
                        emailEditText.getText().toString()
                );
            }
        }
    }

    public boolean checkEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void insertNewUser(String name, Boolean isAdmin, String username, String email, String password) {
        //Create a new object with the new user values
        User user = new User(name, isAdmin, username, email, password, 0);

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

    public void modifyUserWithPassword(Integer idUser, String name, Boolean isAdmin, String username, String email, String password) {

        Call<User> call = mAPIService.updateUserWithPassword(
                MainActivity.tokenAuth,
                idUser,
                name,
                isAdmin,
                username,
                email,
                password
        );

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Modificación Usuario Exitoso", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Modificación Usuario Fallido", Toast.LENGTH_LONG).show();
                    Log.e("ModifyUser", "onFailure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ModifyUser", "onFailure: " + t.getMessage());
            }
        });
    }

    public void modifyUserNoPassword(Integer idUser, String name, Boolean isAdmin, String username, String email) {
        Call<User> call = mAPIService.updateUserNoPassword(
                MainActivity.tokenAuth,
                idUser,
                name,
                isAdmin,
                username,
                email
        );

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Modificación Usuario Exitoso", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Modificación Usuario Fallido", Toast.LENGTH_LONG).show();
                    Log.e("ModifyUser", "onFailure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ModifyUser", "onFailure: " + t.getMessage());
            }
        });
    }
}
