package com.example.malhar_rollcount;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

import android.os.Bundle;

public class Histogram extends AppCompatActivity {
    private BarChart barChart;
    private SQLiteDatabase dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram);
        dbh = this.openOrCreateDatabase("myData", MODE_PRIVATE, null);

        barChart = (BarChart) findViewById(R.id.barGraph);
        ArrayList<BarEntry> entries = new ArrayList<>();

        String table_name = getIntent().getStringExtra("histo");
        Cursor c = dbh.rawQuery("SELECT * FROM [" + table_name+"]", null);

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int sum = 0;
        int entry_data;
        int id = c.getColumnIndex("id");
        int entry = c.getColumnIndex("entry");
        while (c.moveToNext()) {
            entry_data = c.getInt(entry);
            entries.add(new BarEntry(Float.parseFloat(c.getString(id)), entry_data));
            if(entry_data > max){
                max = entry_data;
            }if(entry_data < min){
                min = entry_data;
            }
            sum += entry_data;
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Dice Roll");

        ArrayList<BarEntry> theData = new ArrayList<>();

        for (int i = 1; i <= 18; i++) {
            theData.add(new BarEntry(i, 0));
        }
        TextView textView1 = findViewById(R.id.textViewMax);
        textView1.setText("Maximum "+max);

        TextView textView2 = findViewById(R.id.textViewMin);
        textView2.setText("Minimum "+min);

        TextView textView3 = findViewById(R.id.textViewAvg);
        textView3.setText("Average");

        barDataSet.setColors(new int[] {Color.RED, Color.GREEN, Color.GRAY, Color.BLACK, Color.BLUE});

        BarDataSet barDataSet1 = new BarDataSet(theData, "Dice");

        BarData org = new BarData(barDataSet1, barDataSet);
        barChart.setData(org);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);


    }
}


