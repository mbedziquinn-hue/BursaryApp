package com.example.bursaryapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    TextView tvHiUser, tvViewAll;
    LinearLayout btnNavProfile, btnNavSearch, btnNavApplied, btnNavAlerts, bursaryListContainer;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DatabaseReference mRealtimeDb;
    String userField = "", userAverage = "0", userIncome = "999999";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mRealtimeDb = FirebaseDatabase.getInstance().getReference();

        tvHiUser = findViewById(R.id.tvHiUser);
        tvViewAll = findViewById(R.id.tvViewAll);
        btnNavProfile = findViewById(R.id.btnNavProfile);
        btnNavSearch = findViewById(R.id.btnNavSearch);
        btnNavApplied = findViewById(R.id.btnNavApplied);
        btnNavAlerts = findViewById(R.id.btnNavAlerts);
        bursaryListContainer = findViewById(R.id.bursaryListContainer);

        // TRIGGER DATA UPLOAD ONCE (Optional: Remove after first run)
        BursaryData.addBursaries();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getEmail() != null) {
            String email = user.getEmail();
            String name = email.contains("@") ? email.substring(0, email.indexOf("@")) : "User";
            tvHiUser.setText("Hi, " + name + " 👋");

            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(document -> {
                        if (document.exists()) {
                            userField = document.getString("fieldOfStudy") != null ? document.getString("fieldOfStudy") : "";
                            userAverage = document.getString("average") != null ? document.getString("average") : "0";
                            userIncome = document.getString("income") != null ? document.getString("income") : "999999";
                        }
                        loadTop4Bursaries();
                    });
        }

        tvViewAll.setOnClickListener(v -> startActivity(new Intent(this, BursaryMatchActivity.class)));
        btnNavSearch.setOnClickListener(v -> startActivity(new Intent(this, SearchActivity.class)));
        btnNavProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        btnNavApplied.setOnClickListener(v -> startActivity(new Intent(this, AppliedBursariesActivity.class)));
        if (btnNavAlerts != null) {
            btnNavAlerts.setOnClickListener(v -> startActivity(new Intent(this, AlertsActivity.class)));
        }
    }

    private void loadTop4Bursaries() {
        // This still loads from Firestore to keep your existing "Top 4" logic
        db.collection("bursaries").get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) return;
                    bursaryListContainer.removeAllViews();
                    int count = 0;
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        if (count >= 4) break;
                        String name = doc.getString("name");
                        String field = doc.getString("field");
                        String amount = doc.getString("amount");
                        String deadline = doc.getString("deadline");
                        String applyLink = doc.getString("applyLink");
                        long minAvg = doc.getLong("minAverage") != null ? doc.getLong("minAverage") : 0;
                        long maxInc = doc.getLong("maxIncome") != null ? doc.getLong("maxIncome") : 999999;

                        if (qualifies(field, minAvg, maxInc)) {
                            addBursaryCard(name, field, amount, deadline, applyLink);
                            count++;
                        }
                    }
                });
    }

    private boolean qualifies(String field, long minAvg, long maxIncome) {
        try {
            int avg = Integer.parseInt(userAverage.replaceAll("[^0-9]", ""));
            int income = Integer.parseInt(userIncome.replaceAll("[^0-9]", ""));
            if (avg < minAvg || (income * 12) > maxIncome) return false;
            if (field != null && !field.equalsIgnoreCase("Any Field")) {
                return field.toLowerCase().contains(userField.toLowerCase());
            }
            return true;
        } catch (Exception e) { return true; }
    }

    private void addBursaryCard(String name, String field, String amount, String deadline, String applyLink) {
        LinearLayout card = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.setMargins(0, 0, 0, 16);
        card.setLayoutParams(params);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(Color.parseColor("#1A3A5C"));
        card.setPadding(30, 30, 30, 30);

        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextColor(Color.WHITE);
        tvName.setTextSize(18);
        tvName.setTypeface(null, Typeface.BOLD);

        TextView tvDetails = new TextView(this);
        tvDetails.setText(amount + " • Deadline: " + deadline);
        tvDetails.setTextColor(Color.parseColor("#80DEEA"));
        tvDetails.setTextSize(13);
        tvDetails.setPadding(0, 8, 0, 16);

        Button btnApply = new Button(this);
        btnApply.setText("APPLY");
        btnApply.setBackgroundColor(Color.parseColor("#4DB6AC"));
        btnApply.setTextColor(Color.WHITE);

        btnApply.setOnClickListener(v -> {
            saveApplicationToTracker(name, amount, deadline);
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(applyLink));
            startActivity(i);
        });

        card.addView(tvName);
        card.addView(tvDetails);
        card.addView(btnApply);
        bursaryListContainer.addView(card);
    }

    private void saveApplicationToTracker(String bursaryName, String amount, String deadline) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        String dateToday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Map<String, Object> app = new HashMap<>();
        app.put("name", bursaryName); // Use "name" to match your AppliedBursary model
        app.put("appliedDate", dateToday); // Match model
        app.put("status", "Pending");

        String cleanId = bursaryName.replaceAll("[^a-zA-Z0-9]", "");

        // Path: Applications -> USER_ID -> BURSARY_ID
        // This ensures AppliedBursariesActivity can fetch ONLY this user's data
        mRealtimeDb.child("Applications").child(user.getUid()).child(cleanId)
                .setValue(app)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Application tracked!", Toast.LENGTH_SHORT).show());
    }
}