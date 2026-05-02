package com.example.bursaryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AccountCreatedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_created);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvProvince = findViewById(R.id.tvProvince);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String province = getIntent().getStringExtra("province");

        tvWelcome.setText("Welcome, " + name + "! 🎉");
        tvName.setText(name);
        tvEmail.setText(email);
        tvProvince.setText(province);

        Button btnCompleteProfile = findViewById(R.id.btnCompleteProfile);
        btnCompleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountCreatedActivity.this, CompleteProfileActivity.class);
                startActivity(intent);
            }
        });

    }
}