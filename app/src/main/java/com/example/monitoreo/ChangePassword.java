package com.example.monitoreo;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    EditText NewPassword, RepeatedPassword;
    ImageButton CancelChangePassword, SavePassword;
    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //Get the size of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        //With these three line below the Activity will be shown like a little square now using all the space available.
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .60), (int) (height * .40));

        mAPIService = APIUtils.getAPIService();

        //EditTexts
        NewPassword = findViewById(R.id.NewPassword);
        RepeatedPassword = findViewById(R.id.RepeatedPassword);
        //ImageButtons
        CancelChangePassword = findViewById(R.id.CancelChangePassword);
        SavePassword = findViewById(R.id.SavePassword);

        CancelChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void checkPasswords(View view){
        String newPass = NewPassword.getText().toString();
        String repeatPass = RepeatedPassword.getText().toString();
        System.out.println(newPass + " " + repeatPass);
        if (!newPass.equals(repeatPass)) {
            Toast.makeText(getApplicationContext(), "Contraseña Invalida", Toast.LENGTH_LONG).show();
            return;
        }
        savePassword(newPass);
    }

    public void savePassword(String pass){
        Call<ResponseBody> call = mAPIService.changePassword(MainActivity.tokenAuth, pass);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Contraseña cambiada", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error al cambiar la contraseña", Toast.LENGTH_LONG).show();
                    Log.e("Change Password", "onFailure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Change Password", "onFailure: " + t.getMessage());
            }
        });
    }
}
