package com.example.monitoreo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoreo.data.model.User;

public class UserData extends AppCompatActivity {
    TextView nameTextView, emailTextView, userTextView, roleTextView;

    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        //TextViews
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        userTextView = findViewById(R.id.userTextView);
        roleTextView = findViewById(R.id.roleTextView);

        Bundle objectSent = getIntent().getExtras();

        if (objectSent != null) {
            user = (User) objectSent.getSerializable("user");

            nameTextView.setText(user.getName());
            emailTextView.setText(user.getEmail());
            userTextView.setText(user.getUsername());
            if (user.getAdmin()){
                roleTextView.setText("Administrador");
            } else {
                roleTextView.setText("Normal");
            }

        }
    }
}
