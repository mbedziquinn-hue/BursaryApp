package com.example.bursaryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    TextView tvProfileName, tvProfileEmail, tvProfilePhone, tvProfileGender,
            tvProfileProvince, tvProfileGrade, tvProfileSchool, tvProfileField,
            tvProfileAverage, tvProfileIncome, tvProfileDependants, tvProfileGradeProvince;
    Button btnLogout;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        tvProfilePhone = findViewById(R.id.tvProfilePhone);
        tvProfileGender = findViewById(R.id.tvProfileGender);
        tvProfileProvince = findViewById(R.id.tvProfileProvince);
        tvProfileGrade = findViewById(R.id.tvProfileGrade);
        tvProfileSchool = findViewById(R.id.tvProfileSchool);
        tvProfileField = findViewById(R.id.tvProfileField);
        tvProfileAverage = findViewById(R.id.tvProfileAverage);
        tvProfileIncome = findViewById(R.id.tvProfileIncome);
        tvProfileDependants = findViewById(R.id.tvProfileDependants);
        tvProfileGradeProvince = findViewById(R.id.tvProfileGradeProvince);
        btnLogout = findViewById(R.id.btnLogout);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            tvProfileEmail.setText(user.getEmail());

            String email = user.getEmail();
            String name = email.substring(0, email.indexOf("@"));
            tvProfileName.setText(name);

            db.collection("users").document(user.getUid())
                    .get()
                    .addOnSuccessListener((DocumentSnapshot document) -> {
                        if (document.exists()) {
                            tvProfilePhone.setText(document.getString("phone"));
                            tvProfileGender.setText(document.getString("gender"));
                            tvProfileGrade.setText(document.getString("gradeLevel"));
                            tvProfileSchool.setText(document.getString("school"));
                            tvProfileField.setText(document.getString("fieldOfStudy"));
                            tvProfileAverage.setText(document.getString("average") + "%");
                            tvProfileIncome.setText("R" + document.getString("income") + "/month");
                            tvProfileDependants.setText(document.getString("dependants"));
                            tvProfileGradeProvince.setText(
                                    document.getString("gradeLevel") + " | " +
                                            document.getString("province"));
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ProfileActivity.this,
                                "Failed to load profile", Toast.LENGTH_SHORT).show();
                    });
        }

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}