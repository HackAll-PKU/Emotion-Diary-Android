package org.hackpku.emotiondiary.Stats;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

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
 * Created by kouyuting on 2016/6/6.
 */
public class StatsView extends FrameLayout {
   // private Button thisWeek;//显示这周心情
    //private Button thisMonth;//显示这月心情
    private boolean isThisWeek;

    public StatsView(Context context , AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.activity_stats, this);
        showData();
    }

    /**
     * 显示日期
     */
    public void showData(){
        DiaryHelper diaryHelper = new DiaryHelper();
        LineChart chart = (LineChart) findViewById(R.id.chart);
        int date;
        //过去一个月的统计值心情，不在乎1天2天的
        date = isThisWeek?7:30;
        ArrayList<Double> emtionsRowData = diaryHelper.getHappinessForTimeAsArrayList(date);

        //绘图
        ArrayList<Entry> emotions = new ArrayList<Entry>();
        for(int loop=1;loop<=date;++loop){
            Entry c1=new Entry(emtionsRowData.indexOf(loop),loop);
            emotions.add(c1);
        }
        LineDataSet setEmotions = new LineDataSet(emotions,"My Emotions");
        setEmotions.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setEmotions);

        ArrayList<String> xVals = new ArrayList<String>();

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        chart.invalidate(); // refresh

    }



    }
