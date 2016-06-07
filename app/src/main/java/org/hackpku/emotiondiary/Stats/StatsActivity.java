package org.hackpku.emotiondiary.Stats;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.hackpku.emotiondiary.R;
import org.hackpku.emotiondiary.common.Diary.DiaryHelper;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/6/7.
 */
public class StatsActivity extends AppCompatActivity {
    private Button thisWeek;//显示这周心情
    private Button thisMonth;//显示这月心情
    private LineChart chart;
    private boolean isThisWeek;


    /**
     * 显示页面
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        setTitle("心情统计");
        chart = (LineChart) findViewById(R.id.chart);
        chart.setDrawGridBackground(false);
        chart.setGridBackgroundColor(Color.argb(0, 255, 255, 255));
        chart.setTouchEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawBorders(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.setDescription("一周心情");
        chart.getLegend().setEnabled(false);
        thisWeek = (Button) findViewById(R.id.Week);
        thisMonth = (Button) findViewById(R.id.Month);
        this.isThisWeek = true;
        setOnlistener();//初始化button的监听器
        showData();
    }

    /**
     * 初始化thisWeek和thisMonth按钮点击事件
     */
    private void setOnlistener() {
        thisWeek.setOnClickListener(new View.OnClickListener() {
            @Override  //一周
            public void onClick(View v) {
                isThisWeek = true;
                chart.setDescription("一周心情");
                showData();
            }
        });
        thisMonth.setOnClickListener(new View.OnClickListener() {
            @Override  //一月
            public void onClick(View v) {
                isThisWeek = false;
                chart.setDescription("一月心情");
                showData();
            }
        });
    }

    private void showData() {
        DiaryHelper diaryHelper = new DiaryHelper();
        int date;
        //过去一个月的统计值心情，不在乎1天2天的
        date = isThisWeek ? 7 : 30;
        ArrayList<Double> emotionsRowData = diaryHelper.getHappinessForTimeAsArrayList(date);

        //添加数据绘图
        ArrayList<Entry> emotions = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        for (int loop = 0; loop < date; ++loop) {
            Entry c1 = new Entry(emotionsRowData.get(loop).floatValue(), loop);
            emotions.add(c1);
            xVals.add("");
        }
        LineDataSet setEmotions = new LineDataSet(emotions, "");
        setEmotions.setDrawCubic(true);
        setEmotions.setDrawFilled(true);
        setEmotions.setFillColor(Color.argb(0, 255, 194, 73));
        setEmotions.setFillAlpha(100);
        setEmotions.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setEmotions);

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        chart.invalidate(); // refresh
    }

}
