
package com.example.bursaryapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AlertsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlertAdapter adapter;
    private List<BursaryData> alertList;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        recyclerView = findViewById(R.id.recyclerViewAlerts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        alertList = new ArrayList<>();
        adapter = new AlertAdapter(alertList);
        recyclerView.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference("Bursaries");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                alertList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    BursaryData bursary = data.getValue(BursaryData.class);
                    if (bursary != null) {
                        alertList.add(bursary);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}