package com.example.qrcodeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {
    PieChart pieChart1, pieChart2, pieChart3, pieChart4;
    String key;
    TextView chartKey,chartKey2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Intent intent=getIntent();
        key = intent.getStringExtra("chartKey");
        chartKey = findViewById(R.id.txtChartKey);
        chartKey2 = findViewById(R.id.txtChartKey2);
        chartKey.setText(key.toUpperCase());
        pieChart1 = findViewById(R.id.pieChart1);
        pieChart2 = findViewById(R.id.pieChart2);
        pieChart3 = findViewById(R.id.pieChart3);
        pieChart4 = findViewById(R.id.pieChart4);
        setChart(pieChart1);
        setChart(pieChart2);
        getDataFromFirebase();


    }
    private void setChart(PieChart pieChart){
//        pieChart.getDescription().setText("");
        pieChart.setDrawSliceText(false);
//        pieChart.setUsePercentValues(true);
        pieChart.setHoleRadius(0f);//Draw center circle
        pieChart.setTransparentCircleAlpha(0);//Draw circle outside
        pieChart.setEntryLabelTextSize(15);
    }
    private void getDataFromFirebase() {
        List<PieEntry> entries = new ArrayList<>();
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference(key);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count=0;
                for (DataSnapshot dss : dataSnapshot.getChildren())
                {
                    entries.clear();
                    count++;
                    entries.add(new PieEntry(Float.parseFloat(dss.child("pendingQuantity").getValue().toString()), "Pending"));
                    entries.add(new PieEntry(Float.parseFloat(dss.child("inProgressQuantity").getValue().toString()), "In Progress"));
                    entries.add(new PieEntry(Float.parseFloat(dss.child("quantity").getValue().toString()), "Done"));
                    switch (count){
                        case 1:
                            pieChart1.getDescription().setText(dss.child("name").getValue().toString());
                            dataSet(entries, pieChart1);
                            chartKey.setText(dss.child("pendingQuantity").getValue().toString()+dss.child("inProgressQuantity").getValue().toString()+dss.child("quantity").getValue());
                            break;
                        case 2:
                            pieChart2.getDescription().setText(dss.child("name").getValue().toString());
                            dataSet(entries, pieChart2);
                            chartKey2.setText(dss.child("pendingQuantity").getValue().toString()+dss.child("inProgressQuantity").getValue().toString()+dss.child("quantity").getValue());
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void dataSet(List<PieEntry> entries, PieChart pieChart) {
        PieDataSet pieDataSet = new PieDataSet(entries,"");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.MAGENTA);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);

        pieDataSet.setColors(colors);
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}