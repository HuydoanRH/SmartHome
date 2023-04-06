package com.yourdomain.company.aimyhome.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.kaku.wcv.WeatherChartView;
import com.yourdomain.company.aimyhome.R;

public class ChartFragment extends Fragment {
    GraphView  graph ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        final View view =  inflater.inflate(R.layout.fragment_chart,container,false);
        graph = (GraphView)view.findViewById(R.id.graph);
        LineGraphSeries series = new LineGraphSeries();
        graph.addSeries(series);
        double x = 0;
        double y = 5;
        DataPoint pointValue = new DataPoint(x, y);
        return view;
    }


}