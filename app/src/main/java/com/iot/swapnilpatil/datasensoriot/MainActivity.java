package com.iot.swapnilpatil.datasensoriot;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static List<ParseObject> allObjects = new ArrayList<ParseObject>();
    private List<Date> strings = new ArrayList<Date>();
    private List<Float> values = new ArrayList<Float>();
    private List<Float> hvalues = new ArrayList<Float>();
    private List<Entry> entries = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "*********************", "************************");

        // Following code snippet allows you to test parse connectivity and ability to create object
        /*
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
        */
        findObjects(0);
        findObjects(1000);
        findObjects(2000);

        //findObjects(100);
        //findObjects(200);

        //System.out.println("Allobjects size"+allObjects.size());

        /*ParseQuery<ParseObject> query = ParseQuery.getQuery("sensordata");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH)-1);
        query.setLimit(2000);
        query.whereGreaterThan("createdAt",cal.getTime());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                List<Entry> entries = new ArrayList<>();
                List<String> hours = new ArrayList<String>();
                if (e == null) {
                    Log.d("temp", "Retrieved " + scoreList.size() + " temp");
                    for(int i = 0; i < scoreList.size(); i++)
                    {
                        System.out.println("temperature: "+scoreList.get(i).getDouble("temp"));
                        if(i%45==0) {
                            entries.add(new Entry((float) i, (float) scoreList.get(i).getDouble("temp")));
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(scoreList.get(i).getCreatedAt());
                            int minute = cal.get(Calendar.MINUTE);
                            String minuteWord = ""+minute;
                            if(minuteWord.length()==1)
                                minuteWord = "0" + minute;
                            String word = cal.get(Calendar.HOUR_OF_DAY) + ":" + minuteWord.charAt(0)+"0";
                            hours.add(0,word);
                        }
                    }

                    makeChart(entries, hours);
                    System.out.println(hours.toString());
                    System.out.println("worked");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                    System.out.println("didn't work");
                }
            }
        });*/
    }

    public void findObjects(final int skip)
    {
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("sensordata");
        parseQuery.setLimit(1000);
        parseQuery.setSkip(skip);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
        parseQuery.whereGreaterThan("createdAt", cal.getTime());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for(int i = 0; i < objects.size();i++)
                    {
                        strings.add(i,objects.get(i).getCreatedAt());
                        values.add(i,(float) objects.get(i).getDouble("temp"));
                        hvalues.add(i,(float)objects.get(i).getDouble("humidity"));
                        allObjects.add(i,objects.get(i));
                    }
                    System.out.println("Printers: "+objects.size());
                    System.out.println("AllObject size swag: "+ allObjects.size());
                    if(skip==0) {
                        System.out.println(strings);
                        System.out.println(strings.size());
                        System.out.println(values);
                        System.out.println(values.size());
                        System.out.println(allObjects.size());
                        List<Entry> entries = new ArrayList<>();
                        List<Entry> hentries = new ArrayList<>();
                        List<String> hours = new ArrayList<String>();
                        for(int i = 0; i < allObjects.size(); i++)
                            {
                                //System.out.println("temperature: "+allObjects.get(i).getDouble("temp"));
                                if(i%45==0) {
                                    entries.add(new Entry((float) i, values.get(i)));
                                    hentries.add(new Entry((float)i, hvalues.get(i)));
                                    //System.out.println("Index: "+i);
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(allObjects.get(i).getCreatedAt());
                                    int minute = cal.get(Calendar.MINUTE);
                                    String minuteWord = "" + minute;
                                    if (minuteWord.length() == 1)
                                        minuteWord = "0" + minute;
                                    String word = cal.get(Calendar.HOUR_OF_DAY) + ":" + minuteWord.charAt(0) + "0";
                                    hours.add(0, word);
                                }
                            }
                        System.out.println(entries);
                        makeChart(hentries,entries, hours);
                    }
                } else {
                }
            }
        });
    }

    void makeChart(List<Entry> hentries, List<Entry> entries, final List<String> hours)
    {
        /*List<Entry> entries2 = new ArrayList<Entry>();
        for(int i = 0; i < allObjects.size(); i++)
        {
            if(i%45==0)
                entries2.add(new Entry((float)(i), values.get(i)));
                //entries2.add(new Entry((float)(i), (float)i));
        }*/
        LineChart chart = (LineChart) findViewById(R.id.chart);

        LineDataSet dataSet = new LineDataSet(entries,"Label");
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawValues(false);
        dataSet.setCircleRadius(3f);
        dataSet.setColor(Color.RED);


        LineDataSet hdataSet = new LineDataSet(hentries,"Label");
        hdataSet.setDrawCircleHole(false);
        hdataSet.setDrawValues(false);
        hdataSet.setCircleRadius(3f);
        hdataSet.setColor(Color.RED);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.RED);

        ArrayList<LineDataSet> lines = new ArrayList<LineDataSet> ();
        lines.add(dataSet);
        lines.add(hdataSet);

        LineData lineData = new LineData(hdataSet);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setTextColor(Color.RED);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(true);
        rightAxis.setTextColor(Color.RED);

        AxisValueFormatter axisValueFormatter = new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String word = ""+value;
                int num2 = (int) ((value/allObjects.size())*hours.size());
                if(num2>=0)
                    word = hours.get(num2);
                //System.out.println("Value: "+value);
                return word;
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        };
        xAxis.setValueFormatter(axisValueFormatter);

        chart.setData(lineData);
        chart.setPinchZoom(true);
        chart.setDragEnabled(true);
        chart.setDragDecelerationFrictionCoef((float) 0.01);
        chart.setDescription("Sensor Data");
        chart.setDescriptionPosition(250f,15f);
        chart.setDescriptionColor(Color.RED);
        chart.setDrawGridBackground(false);
        chart.invalidate();
    }
}
