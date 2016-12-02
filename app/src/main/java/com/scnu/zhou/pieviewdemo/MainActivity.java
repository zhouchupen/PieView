package com.scnu.zhou.pieviewdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.scnu.zhou.widget.PieView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PieView pieView = (PieView) findViewById(R.id.pieView);

        List<PieView.PieData> mData = new ArrayList<>();
        PieView.PieData data1 = new PieView.PieData("blue", 50, Color.BLUE);
        PieView.PieData data2 = new PieView.PieData("red", 30, Color.RED);
        PieView.PieData data3 = new PieView.PieData("green", 30, Color.GREEN);
        PieView.PieData data4 = new PieView.PieData("yellow", 60, Color.YELLOW);
        PieView.PieData data5 = new PieView.PieData("black", 40, Color.BLACK);

        mData.add(data1);
        mData.add(data2);
        mData.add(data3);
        mData.add(data4);
        mData.add(data5);

        pieView.setData(mData);
    }
}
