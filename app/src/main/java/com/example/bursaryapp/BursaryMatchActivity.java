package com.example.bursaryapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BursaryMatchActivity extends AppCompatActivity {

    LinearLayout bursaryContainer;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String userField, userAverage, userIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bursary_match);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        bursaryContainer = findViewById(R.id.bursaryContainer);

        loadUserProfileAndMatch();
    }

    private void loadUserProfileAndMatch() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        userField = document.getString("fieldOfStudy");
                        userAverage = document.getString("average");
                        userIncome = document.getString("income");
                        loadBursaries();
                    } else {
                        Toast.makeText(this,
                                "Please complete your profile first!",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    private void loadBursaries() {
        db.collection("bursaries").get()
                .addOnSuccessListener(querySnapshot -> {
                    bursaryContainer.removeAllViews();
                    int matchCount = 0;

                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String name = doc.getString("name");
                        String field = doc.getString("field");
                        String amount = doc.getString("amount");
                        String deadline = doc.getString("deadline");
                        String description = doc.getString("description");
                        String applyLink = doc.getString("applyLink");
                        String status = doc.getString("status");
                        long minAverage = doc.getLong("minAverage");
                        long maxIncome = doc.getLong("maxIncome");

                        // CHECK IF USER QUALIFIES
                        boolean qualifies = checkQualification(
                                field, minAverage, maxIncome);

                        if (qualifies) {
                            matchCount++;
                            addBursaryCard(name, field, amount,
                                    deadline, description, applyLink,
                                    status, matchCount);
                        }
                    }

                    if (matchCount == 0) {
                        TextView noMatch = new TextView(this);
                        noMatch.setText(
                                "No matches found. Update your profile!");
                        noMatch.setTextColor(0xFFFFFFFF);
                        noMatch.setPadding(20, 20, 20, 20);
                        bursaryContainer.addView(noMatch);
                    }
                });
    }

    private boolean checkQualification(
            String bursaryField, long minAverage, long maxIncome) {
        try {
            int avg = Integer.parseInt(userAverage);
            int income = Integer.parseInt(userIncome);

            // Check average
            if (avg < minAverage) return false;

            // Check income (monthly * 12 = annual)
            if (income * 12 > maxIncome) return false;

            // Check field of study
            if (!bursaryField.equalsIgnoreCase("Any Field")) {
                String[] fields = bursaryField.split(",");
                boolean fieldMatch = false;
                for (String f : fields) {
                    if (userField.toLowerCase()
                            .contains(f.trim().toLowerCase()) ||
                            f.trim().toLowerCase()
                                    .contains(userField.toLowerCase())) {
                        fieldMatch = true;
                        break;
                    }
                }
                if (!fieldMatch) return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void addBursaryCard(String name, String field,
                                String amount, String deadline, String description,
                                String applyLink, String status, int matchNum) {

        // Check deadline
        String deadlineStatus = getDeadlineStatus(deadline);
        int cardColor = 0xFF1A3A5C;
        if (deadlineStatus.contains("Closed")) {
            cardColor = 0xFF4A1A1A;
        } else if (deadlineStatus.contains("Closing")) {
            cardColor = 0xFF4A3A1A;
        }

        // Create card
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(cardColor);
        card.setPadding(30, 25, 30, 25);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 20);
        card.setLayoutParams(params);

        // Match number
        TextView tvMatch = new TextView(this);
        tvMatch.setText("Match #" + matchNum + " ✓");
        tvMatch.setTextColor(0xFF4DB6AC);
        tvMatch.setTextSize(12);
        card.addView(tvMatch);

        // Bursary name
        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextColor(0xFFFFFFFF);
        tvName.setTextSize(18);
        tvName.setPadding(0, 8, 0, 4);
        card.addView(tvName);

        // Field
        TextView tvField = new TextView(this);
        tvField.setText("📚 " + field);
        tvField.setTextColor(0xFF80DEEA);
        tvField.setTextSize(13);
        card.addView(tvField);

        // Amount
        TextView tvAmount = new TextView(this);
        tvAmount.setText("💰 " + amount);
        tvAmount.setTextColor(0xFFFFFFFF);
        tvAmount.setTextSize(14);
        tvAmount.setPadding(0, 8, 0, 0);
        card.addView(tvAmount);

        // Deadline status
        TextView tvDeadline = new TextView(this);
        tvDeadline.setText("📅 " + deadlineStatus);
        if (deadlineStatus.contains("Closed")) {
            tvDeadline.setTextColor(0xFFEF5350);
        } else if (deadlineStatus.contains("Closing")) {
            tvDeadline.setTextColor(0xFFFFD600);
        } else {
            tvDeadline.setTextColor(0xFF66BB6A);
        }
        tvDeadline.setTextSize(13);
        tvDeadline.setPadding(0, 4, 0, 8);
        card.addView(tvDeadline);

        // Description
        TextView tvDesc = new TextView(this);
        tvDesc.setText(description);
        tvDesc.setTextColor(0xFFB2EBF2);
        tvDesc.setTextSize(13);
        tvDesc.setPadding(0, 4, 0, 12);
        card.addView(tvDesc);

        // Apply button (only if not closed)
        if (!deadlineStatus.contains("Closed")) {
            android.widget.Button btnApply =
                    new android.widget.Button(this);
            btnApply.setText("APPLY NOW →");
            btnApply.setTextColor(0xFF000000);
            btnApply.setBackgroundColor(0xFF4DB6AC);
            btnApply.setOnClickListener(v -> {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(applyLink));
                startActivity(browserIntent);
            });
            card.addView(btnApply);
        } else {
            TextView tvClosed = new TextView(this);
            tvClosed.setText("❌ This bursary is CLOSED");
            tvClosed.setTextColor(0xFFEF5350);
            tvClosed.setTextSize(13);
            card.addView(tvClosed);
        }

        bursaryContainer.addView(card);
    }

    private String getDeadlineStatus(String deadline) {
        try {
            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date deadlineDate = sdf.parse(deadline);
            Date today = new Date();

            long diff = deadlineDate.getTime() - today.getTime();
            long daysLeft = diff / (1000 * 60 * 60 * 24);

            if (daysLeft < 0) {
                return "❌ Closed";
            } else if (daysLeft <= 30) {
                return "⚠️ Closing Soon — " + daysLeft + " days left!";
            } else {
                return "✅ Open — Deadline: " + deadline +
                        " (" + daysLeft + " days left)";
            }
        } catch (Exception e) {
            return "Deadline: " + deadline;
        }
    }
}