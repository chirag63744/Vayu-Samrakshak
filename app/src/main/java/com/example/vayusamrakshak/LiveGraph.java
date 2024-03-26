package com.example.vayusamrakshak;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LiveGraph extends AppCompatActivity {

    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    private DatabaseReference databaseReference;
    ArrayList<Entry> lineEntries = new ArrayList<>(); // Declare ArrayList with Entry type

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_graph);

        // Initial dummy data point (optional)
        lineEntries.add(new Entry(0f, 0f));

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vayusamrakshak-default-rtdb.firebaseio.com/carbonEmission");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve carbonEmission value from dataSnapshot
                Float carbonEmission = dataSnapshot.getValue(Float.class);
                Toast.makeText(LiveGraph.this, "" + carbonEmission, Toast.LENGTH_SHORT).show();
                lineEntries.add(new Entry(lineEntries.size(), carbonEmission));
                lineDataSet = new LineDataSet(lineEntries, "");
                lineData = new LineData(lineDataSet);
                lineChart.setData(lineData);
                lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                lineDataSet.setValueTextColor(Color.BLACK);
                lineDataSet.setValueTextSize(18f);

                // Update chart with the new data
                lineDataSet.notifyDataSetChanged();
                lineChart.notifyDataSetChanged();
                lineChart.invalidate(); // Refreshes the chart
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lineChart = findViewById(R.id.lineChart);

//        lineDataSet = new LineDataSet(lineEntries, "");
//        lineData = new LineData(lineDataSet);
//        lineChart.setData(lineData);
//        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//        lineDataSet.setValueTextColor(Color.BLACK);
//        lineDataSet.setValueTextSize(18f);
    }

    private void getEntries(float carbonEmission) {
        // Add new data point with received carbonEmission value
        lineEntries.add(new Entry(lineEntries.size(), carbonEmission)); // Use current data point count for x-axis (timestamp)
    }
}
