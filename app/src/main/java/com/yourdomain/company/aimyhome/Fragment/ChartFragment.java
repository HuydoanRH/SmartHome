package com.yourdomain.company.aimyhome.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.yourdomain.company.aimyhome.MQTTHelper;
import com.yourdomain.company.aimyhome.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.TimeZone;

public class ChartFragment extends Fragment {
    final int HOUR_OF_DAY = 24;
    LineChart tempChart;
    LineChart humidChart;
    private Context context;

    HashMap<Integer, Float> tempHashMap = new HashMap<Integer, Float>() {{
        for (int i = 0; i < HOUR_OF_DAY; i++) {
            put(i, 0.0F);
        }
    }};

    HashMap<Integer, Float> humidHashMap = new HashMap<Integer, Float>() {{
        for (int i = 0; i < HOUR_OF_DAY; i++) {
            put(i, 0.0F);
        }
    }};

    HashMap<Integer, Integer> numOfRecord = new HashMap<Integer, Integer>() {{
        for (int i = 0; i < HOUR_OF_DAY; i++) {
            put(i, 0);
        }
    }};

    public ChartFragment() {

    }

    // Initialise it from onAttach()
    @Override
    public void onAttach(Context context2) {
        super.onAttach(context2);
        context = context2;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Huy",  "onCreateChartView");

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        try {
            curlRequest("temperature");
            curlRequest("humidity");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tempChart = (LineChart) view.findViewById(R.id.tempChart);
        humidChart = (LineChart) view.findViewById(R.id.humidChart);

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.refreshLayout);
        pullToRefresh.setOnRefreshListener(() -> {
            refreshChartData();
            Toast toast = Toast.makeText(context,"Refresh data successfully",Toast. LENGTH_SHORT);
            toast.show();
            pullToRefresh.setRefreshing(false);
        });

        TextView day = view.findViewById(R.id.day);
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        String today = sdf.format(date);
        day.setText(today);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        refreshChartData();
        tempChart.notifyDataSetChanged();
        tempChart.invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void curlRequest(String feeds) throws ParseException {
        Calendar calender = Calendar.getInstance();
        calender.set( Calendar.HOUR_OF_DAY , 0 );
        calender.set( Calendar.MINUTE, 0 );
        Date date = calender.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        String today = format.format(date);
        String tomorrow = getNextDate(today);

        String url = "https://io.adafruit.com/api/v2/DucHuy/feeds/" + feeds + "/data?x-aio-key="+ MQTTHelper.password +"&start_time="
                + today + "&end_time=" + tomorrow;
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> {
            try {
                handleDataResponse(response, feeds);
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }, error -> {

        });
        requestQueue.add(jsonArrayRequest);
    }

    public void handleDataResponse(JSONArray data, String feeds) throws JSONException, ParseException {
        if (data.length() != 0) {
            for (int i = 0; i < data.length(); i++) {
                String str_temp = data.getJSONObject(i).getString("value");
                String time = data.getJSONObject(i).getString("created_at");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = null;
                try {
                    date = dateFormat.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int localTime = calendar.get(Calendar.HOUR_OF_DAY);
                if (feeds == "temperature") {
                    int temp = Integer.parseInt(str_temp);
                    tempHashMap.put(localTime, tempHashMap.get(localTime) + (float) temp);
                    numOfRecord.put(localTime, numOfRecord.get(localTime) + 1);
                }
                else {
                    int humid = Integer.parseInt(str_temp);
                    humidHashMap.put(localTime, humidHashMap.get(localTime) + (float) humid);
                    numOfRecord.put(localTime, numOfRecord.get(localTime) + 1);
                }
            }

            for (int i = 0; i < HOUR_OF_DAY; i++) {
                if (numOfRecord.get(i) == 0)
                    continue;
                if (feeds == "temperature"){
                    tempHashMap.put(i, tempHashMap.get(i) / numOfRecord.get(i));
                }
                else humidHashMap.put(i, humidHashMap.get(i) / numOfRecord.get(i));
            }

            handleDataChart(tempHashMap, "Temperature", tempChart, Color.RED);
            handleDataChart(humidHashMap, "Humidity", humidChart, Color.BLUE);
        }
    }

    public void handleDataChart(HashMap hashMap, String name, LineChart lineChart, int color) throws ParseException {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        for (int i = 0; i < hashMap.size(); i++) {
            dataVals.add(new Entry(i, (float) hashMap.get(i)));
        }
        configChart(dataVals, name, lineChart, color);
    }

    public void configChart(ArrayList<Entry> dataVals, String name, LineChart lineChart, int color) {
        LineDataSet lineDataSet = new LineDataSet(dataVals, name);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(color);
        lineDataSet.setFillAlpha(15);
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setCircleHoleColor(color);
        lineDataSet.setCircleRadius(2f);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setCubicIntensity(1f);


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        data.setValueTextSize(13f);
        data.setDrawValues(false);

        lineChart.setDescription(null);
        lineChart.animateY(300);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setAxisMaximum(100f);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setData(data);
        lineChart.invalidate();
        lineChart.setHighlightPerTapEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        MyMarkerView marker = new MyMarkerView(getActivity(), R.layout.content);
        lineChart.setMarker(marker);
    }

    public static String getNextDate(String curDate) throws ParseException {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        Date date = null;
        try {
            date = format.parse(curDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        final Date date = format.parse(curDate);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return format.format(calendar.getTime());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshChartData() {
        try {
            for(int i = 0; i < HOUR_OF_DAY; i++) {
                tempHashMap.put(i, 0.0F);
                humidHashMap.put(i, 0.0F);
                numOfRecord.put(i, 0);
            }
            curlRequest("temperature");
            curlRequest("humidity");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

