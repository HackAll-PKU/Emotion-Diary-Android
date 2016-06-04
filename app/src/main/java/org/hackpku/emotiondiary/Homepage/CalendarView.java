package org.hackpku.emotiondiary.Homepage;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import org.hackpku.emotiondiary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu Zhengyuan on 2016/5/31.
 */
public class CalendarView  extends FrameLayout {
    private ImageView iv_left;  //进入上个月
    private ImageView iv_right;  //进入下个月
    private TextView tv_date;  //显示当天的日期
    private TextView tv_week;  //显示当天的星期
    private TextView tv_today;  //"今天"按钮
    private MonthDateView monthDateView;  //月历视图

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.calendar, this);
        List<Integer> hasThingList = new ArrayList<Integer>();  //记录需要标记日期的List，可用来表示当天有日记的日期
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        monthDateView = (MonthDateView) findViewById(R.id.monthDateView);
        tv_date = (TextView) findViewById(R.id.date_text);
        tv_week  =(TextView) findViewById(R.id.week_text);
        tv_today = (TextView) findViewById(R.id.tv_today);
        monthDateView.setTextView(tv_date,tv_week);
        monthDateView.setDaysHasThingList(hasThingList);
        setOnlistener();  //初始化monthDateView各按钮点击事件
    }

    /**
     * 初始化monthDateView各按钮点击事件
     */
    private void setOnlistener(){
        iv_left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                monthDateView.onLeftClick();
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                monthDateView.onRightClick();
            }
        });

        tv_today.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                monthDateView.setTodayToView();
            }
        });
    }

    /**
     * 设置日期点击事件
     * @param dataClick
     */
    public void setDateClick(MonthDateView.DateClick dataClick){
        monthDateView.setDateClick(dataClick);
    }

    /**
     * 获取用户选中年
     * @return
     */
    public int getSelYear(){
        return monthDateView.getmSelYear();
    }

    /**
     * 获取用户选中月
     * @return
     */
    public int getSelMonth() {
        return monthDateView.getmSelMonth() + 1;
    }

    /**
     * 获取用户选中日
     * @return
     */
    public int getSelDay(){
        return monthDateView.getmSelDay();
    }
}
