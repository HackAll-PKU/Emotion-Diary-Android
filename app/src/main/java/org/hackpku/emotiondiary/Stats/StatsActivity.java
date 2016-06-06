package org.hackpku.emotiondiary.Stats;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import org.hackpku.emotiondiary.R;
import org.hackpku.emotiondiary.common.Diary.DiaryHelper;

/**
 * Created by lenovo on 2016/6/7.
 */
public class StatsActivity extends FragmentActivity {
    private Button thisWeek;//显示这周心情
    private Button thisMonth;//显示这月心情
    private StatsView statsView;
    private boolean isThisWeek;

    /**
     *显示页面
     */
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        statsView =(StatsView) findViewById(R.id.chart);
        thisWeek = (Button) findViewById(R.id.Week);
        thisMonth = (Button) findViewById(R.id.Month);
        isThisWeek = true;
        setOnlistener();//初始化button的监听器
        statsView.showData();
    }
    /**
     * 初始化thisWeek和thisMonth按钮点击事件
     */

    private void setOnlistener(){
        thisWeek.setOnClickListener(new View.OnClickListener(){
            @Override  //一周
            public void onClick(View v) {
                isThisWeek = true;
            }
        });
        thisWeek.setOnClickListener(new View.OnClickListener(){
            @Override  //一月
            public void onClick(View v) {
                isThisWeek = false;
            }
        });
    }


}
