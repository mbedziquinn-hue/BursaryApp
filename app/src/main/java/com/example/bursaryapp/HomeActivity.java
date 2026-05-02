package com.example.bursaryapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    TextView tvHiUser, tvViewAll;
    LinearLayout btnNavProfile, btnNavSearch, bursaryListContainer;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String userField = "", userAverage = "0", userIncome = "999999";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tvHiUser = findViewById(R.id.tvHiUser);
        tvViewAll = findViewById(R.id.tvViewAll);
        btnNavProfile = findViewById(R.id.btnNavProfile);
        btnNavSearch = findViewById(R.id.btnNavSearch);
        bursaryListContainer = findViewById(R.id.bursaryListContainer);

        // Add bursaries to Firestore
        BursaryData.addBursaries();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String name = email.substring(0, email.indexOf("@"));
            tvHiUser.setText("Hi, " + name + " 👋");

            // Load user profile then show top 4 matches
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(document -> {
                        if (document.exists()) {
                            userField = document.getString("fieldOfStudy") != null ?
                                    document.getString("fieldOfStudy") : "";
                            userAverage = document.getString("average") != null ?
                                    document.getString("average") : "0";
                            userIncome = document.getString("income") != null ?
                                    document.getString("income") : "999999";
                        }
                        loadTop4Bursaries();
                    });
        }

        // View All → BursaryMatchActivity
        tvViewAll.setOnClickListener(v -> {
            startActivity(new Intent(this, BursaryMatchActivity.class));
        });

        // Search button
        btnNavSearch.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchActivity.class));
        });

        // Profile button
        btnNavProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });
    }

    private void loadTop4Bursaries() {
        db.collection("bursaries").get()
                .addOnSuccessListener(querySnapshot -> {
                    bursaryListContainer.removeAllViews();
                    int count = 0;

                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        if (count >= 4) break;

                        String name = doc.getString("name");
                        String field = doc.getString("field");
                        String amount = doc.getString("amount");
                        String deadline = doc.getString("deadline");
                        String applyLink = doc.getString("applyLink");
                        long minAverage = doc.getLong("minAverage") != null ?
                                doc.getLong("minAverage") : 0;
                        long maxIncome = doc.getLong("maxIncome") != null ?
                                doc.getLong("maxIncome") : 999999;

                        if (qualifies(field, minAverage, maxIncome)) {
                            addBursaryCard(name, field, amount,
                                    deadline, applyLink);
                            count++;
                        }
                    }
                });
    }

    private boolean qualifies(String field, long minAvg, long maxIncome) {
        try {
            int avg = Integer.parseInt(userAverage);
            int income = Integer.parseInt(userIncome);
            if (avg < minAvg) return false;
            if (income * 12 > maxIncome) return false;
            if (!field.equalsIgnoreCase("Any Field")) {
                String[] fields = field.split(",");
                boolean match = false;
                for (String f : fields) {
                    if (userField.toLowerCase().contains(
                            f.trim().toLowerCase()) ||
                            f.trim().toLowerCase().contains(
                                    userField.toLowerCase())) {
                        match = true;
                        break;
                    }
                }
                if (!match) return false;
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    private void addBursaryCard(String name, String field,
                                String amount, String deadline, String applyLink) {

        String deadlineStatus = getDeadlineStatus(deadline);
        int cardColor = 0xFF1A3A5C;
        if (deadlineStatus.contains("Closed")) cardColor = 0xFF4A1A1A;
        else if (deadlineStatus.contains("Closing")) cardColor = 0xFF4A3A1A;

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setBackgroundColor(cardColor);
        card.setPadding(24, 20, 24, 20);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 12);
        card.setLayoutParams(params);

        LinearLayout leftSide = new LinearLayout(this);
        leftSide.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams leftParams =
                new LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        leftSide.setLayoutParams(leftParams);

        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextColor(0xFFFFFFFF);
        tvName.setTextSize(15);
        tvName.setPadding(0, 0, 0, 4);
        leftSide.addView(tvName);

        TextView tvField = new TextView(this);
        tvField.setText(field.split(",")[0]);
        tvField.setTextColor(0xFF80DEEA);
        tvField.setTextSize(12);
        leftSide.addView(tvField);

        TextView tvDeadline = new TextView(this);
        tvDeadline.setText(deadlineStatus);
        if (deadlineStatus.contains("Closed")) {
            tvDeadline.setTextColor(0xFFEF5350);
        } else if (deadlineStatus.contains("Closing")) {
            tvDeadline.setTextColor(0xFFFFD600);
        } else {
            tvDeadline.setTextColor(0xFF66BB6A);
        }
        tvDeadline.setTextSize(11);
        tvDeadline.setPadding(0, 4, 0, 0);
        leftSide.addView(tvDeadline);

        card.addView(leftSide);

        LinearLayout rightSide = new LinearLayout(this);
        rightSide.setOrientation(LinearLayout.VERTICAL);
        rightSide.setGravity(android.view.Gravity.CENTER);

        TextView tvAmount = new TextView(this);
        tvAmount.setText(amount);
        tvAmount.setTextColor(0xFFFFFFFF);
        tvAmount.setTextSize(12);
        tvAmount.setPadding(0, 0, 0, 8);
        rightSide.addView(tvAmount);

        if (!deadlineStatus.contains("Closed")) {
            android.widget.Button btnApply =
                    new android.widget.Button(this);
            btnApply.setText("APPLY");
            btnApply.setTextColor(0xFF000000);
            btnApply.setTextSize(11);
            btnApply.setBackgroundColor(0xFF4DB6AC);
            btnApply.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(applyLink));
                startActivity(i);
            });
            rightSide.addView(btnApply);
        }

        card.addView(rightSide);
        bursaryListContainer.addView(card);
    }

    private String getDeadlineStatus(String deadline) {
        try {
            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date deadlineDate = sdf.parse(deadline);
            Date today = new Date();
            long diff = deadlineDate.getTime() - today.getTime();
            long daysLeft = diff / (1000 * 60 * 60 * 24);
            if (daysLeft < 0) return "❌ Closed";
            else if (daysLeft <= 30) return "⚠️ Closing — " + daysLeft + "d left";
            else return "✅ Open — " + daysLeft + " days left";
        } catch (Exception e) {
            return deadline;
        }
    }
}