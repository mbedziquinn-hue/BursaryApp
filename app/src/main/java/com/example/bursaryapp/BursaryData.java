package com.example.bursaryapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class BursaryData {
    private String id;
    private String name;
    private String field;
    private String deadline;
    private String description;
    private String status;

    public BursaryData() {}

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getField() { return field; }
    public String getDeadline() { return deadline; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setField(String field) { this.field = field; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }

    /**
     * Call this once to populate your Realtime Database with bursary options.
     */
    public static void addBursaries() {
        // Pointing to "Bursaries" node in Realtime Database
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Bursaries");

        Object[][] bursaries = {
                {"1", "NSFAS Bursary", "Any Field", "2026-11-30", "Open"},
                {"2", "Sasol Bursary", "Engineering, IT", "2026-08-31", "Open"},
                {"3", "Eskom Bursary", "Engineering", "2026-09-15", "Open"},
                {"4", "Absa Fellowship", "Commerce", "2026-07-20", "Open"},
                {"5", "Trans Hex Bursary", "Mining", "2026-05-30", "Open"}
        };

        for (Object[] b : bursaries) {
            String bId = (String) b[0];
            Map<String, Object> map = new HashMap<>();
            map.put("id", bId);
            map.put("name", b[1]);
            map.put("field", b[2]);
            map.put("deadline", b[3]);
            map.put("status", b[4]);

            // Save to Realtime Database
            dbRef.child(bId).setValue(map);
        }
    }
}