package org.hackpku.emotiondiary.Stats;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.github.mikephil.charting.charts.LineChart;
import org.hackpku.emotiondiary.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by kouyuting on 2016/6/6.
 */
public class StatsView extends FrameLayout {
    private Button thisWeek;//显示这周心情
    private Button thisMonth;//显示这月心情

    public StatsView(Context context , AttributeSet attrs){
        super(context,attrs);



    }



    LineChart chart = (LineChart) findViewById(R.id.chart);



}
