package com.example.bursaryapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AppliedBursariesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppliedBursaryAdapter adapter;
    private List<AppliedBursary> list;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_bursaries);

        // Updated ID to match your XML: rvAppliedBursaries
        recyclerView = findViewById(R.id.rvAppliedBursaries);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new AppliedBursaryAdapter(list);
        recyclerView.setAdapter(adapter);

        // Connect to Firebase Realtime Database
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // This points to the database node you saw in Screenshot (222).jpg
            dbRef = FirebaseDatabase.getInstance().getReference("Applications").child(userId);

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        AppliedBursary app = data.getValue(AppliedBursary.class);
                        if (app != null) {
                            list.add(app);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database read errors here
                }
            });
        }
    }
}