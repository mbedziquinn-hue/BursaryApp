package com.example.bursaryapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    EditText edtSearch;
    LinearLayout searchResultsContainer;
    TextView tvResultCount;
    FirebaseFirestore db;
    List<Map<String, Object>> allBursaries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = FirebaseFirestore.getInstance();
        edtSearch = findViewById(R.id.edtSearch);
        searchResultsContainer = findViewById(R.id.searchResultsContainer);
        tvResultCount = findViewById(R.id.tvResultCount);

        loadAllBursaries();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBursaries(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadAllBursaries() {
        db.collection("bursaries").get()
                .addOnSuccessListener(querySnapshot -> {
                    allBursaries.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        allBursaries.add(doc.getData());
                    }
                    tvResultCount.setText(
                            allBursaries.size() + " bursaries available — type to search");
                });
    }

    private void filterBursaries(String query) {
        searchResultsContainer.removeAllViews();

        if (query.isEmpty()) {
            tvResultCount.setText(
                    allBursaries.size() + " bursaries available — type to search");
            return;
        }

        List<Map<String, Object>> results = new ArrayList<>();
        for (Map<String, Object> bursary : allBursaries) {
            String name = (String) bursary.get("name");
            String field = (String) bursary.get("field");

            if (name.toLowerCase().contains(query.toLowerCase()) ||
                    field.toLowerCase().contains(query.toLowerCase())) {
                results.add(bursary);
            }
        }

        tvResultCount.setText(results.size() + " results for \"" + query + "\"");

        if (results.isEmpty()) {
            TextView noResult = new TextView(this);
            noResult.setText("No bursaries found for \"" + query + "\"");
            noResult.setTextColor(0xFFFFFFFF);
            noResult.setPadding(20, 30, 20, 20);
            noResult.setTextSize(15);
            searchResultsContainer.addView(noResult);
        } else {
            for (Map<String, Object> bursary : results) {
                addSearchCard(bursary);
            }
        }
    }

    private void addSearchCard(Map<String, Object> bursary) {
        String name = (String) bursary.get("name");
        String field = (String) bursary.get("field");
        String amount = (String) bursary.get("amount");
        String deadline = (String) bursary.get("deadline");
        String description = (String) bursary.get("description");
        String applyLink = (String) bursary.get("applyLink");

        String deadlineStatus = getDeadlineStatus(deadline);

        int cardColor = 0xFF1A3A5C;
        if (deadlineStatus.contains("Closed")) cardColor = 0xFF4A1A1A;
        else if (deadlineStatus.contains("Closing")) cardColor = 0xFF4A3A1A;

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(cardColor);
        card.setPadding(30, 25, 30, 25);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16);
        card.setLayoutParams(params);

        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextColor(0xFFFFFFFF);
        tvName.setTextSize(18);
        tvName.setPadding(0, 0, 0, 6);
        card.addView(tvName);

        TextView tvField = new TextView(this);
        tvField.setText("📚 " + field);
        tvField.setTextColor(0xFF80DEEA);
        tvField.setTextSize(13);
        card.addView(tvField);

        TextView tvAmount = new TextView(this);
        tvAmount.setText("💰 " + amount);
        tvAmount.setTextColor(0xFFFFFFFF);
        tvAmount.setTextSize(14);
        tvAmount.setPadding(0, 8, 0, 4);
        card.addView(tvAmount);

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

        TextView tvDesc = new TextView(this);
        tvDesc.setText(description);
        tvDesc.setTextColor(0xFFB2EBF2);
        tvDesc.setTextSize(13);
        tvDesc.setPadding(0, 4, 0, 12);
        card.addView(tvDesc);

        if (!deadlineStatus.contains("Closed")) {
            android.widget.Button btnApply =
                    new android.widget.Button(this);
            btnApply.setText("APPLY NOW →");
            btnApply.setTextColor(0xFF000000);
            btnApply.setBackgroundColor(0xFF4DB6AC);
            btnApply.setOnClickListener(v -> {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW, Uri.parse(applyLink));
                startActivity(browserIntent);
            });
            card.addView(btnApply);
        } else {
            TextView tvClosed = new TextView(this);
            tvClosed.setText("❌ This bursary is CLOSED");
            tvClosed.setTextColor(0xFFEF5350);
            card.addView(tvClosed);
        }

        searchResultsContainer.addView(card);
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
            else if (daysLeft <= 30) return "⚠️ Closing Soon — " + daysLeft + " days left!";
            else return "✅ Open — " + daysLeft + " days left (Deadline: " + deadline + ")";
        } catch (Exception e) {
            return "Deadline: " + deadline;
        }
    }
}