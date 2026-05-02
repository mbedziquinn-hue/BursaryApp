package com.example.bursaryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class CompleteProfileActivity extends AppCompatActivity {

    EditText edtGender, edtPhone, edtGradeLevel, edtSchool,
            edtFieldOfStudy, edtAverage, edtIncome, edtDependants;
    Button btnSaveProfile;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        edtGender = findViewById(R.id.edtGender);
        edtPhone = findViewById(R.id.edtPhone);
        edtGradeLevel = findViewById(R.id.edtGradeLevel);
        edtSchool = findViewById(R.id.edtSchool);
        edtFieldOfStudy = findViewById(R.id.edtFieldOfStudy);
        edtAverage = findViewById(R.id.edtAverage);
        edtIncome = findViewById(R.id.edtIncome);
        edtDependants = findViewById(R.id.edtDependants);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender = edtGender.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String gradeLevel = edtGradeLevel.getText().toString().trim();
                String school = edtSchool.getText().toString().trim();
                String fieldOfStudy = edtFieldOfStudy.getText().toString().trim();
                String average = edtAverage.getText().toString().trim();
                String income = edtIncome.getText().toString().trim();
                String dependants = edtDependants.getText().toString().trim();

                if (gender.isEmpty() || phone.isEmpty() || gradeLevel.isEmpty() ||
                        school.isEmpty() || fieldOfStudy.isEmpty() ||
                        average.isEmpty() || income.isEmpty() || dependants.isEmpty()) {
                    Toast.makeText(CompleteProfileActivity.this,
                            "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userId = mAuth.getCurrentUser().getUid();

                Map<String, Object> profile = new HashMap<>();
                profile.put("gender", gender);
                profile.put("phone", phone);
                profile.put("gradeLevel", gradeLevel);
                profile.put("school", school);
                profile.put("fieldOfStudy", fieldOfStudy);
                profile.put("average", average);
                profile.put("income", income);
                profile.put("dependants", dependants);

                db.collection("users").document(userId)
                        .set(profile)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(CompleteProfileActivity.this,
                                    "Profile saved! 🎉", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CompleteProfileActivity.this,
                                    HomeActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(CompleteProfileActivity.this,
                                    "Failed: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        });
            }
        });
    }
}