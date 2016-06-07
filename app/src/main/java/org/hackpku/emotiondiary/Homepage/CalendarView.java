package org.hackpku.emotiondiary.Homepage;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import org.hackpku.emotiondiary.R;
import org.hackpku.emotiondiary.common.Diary.Diary;
import org.hackpku.emotiondiary.common.Diary.DiaryHelper;

import java.util.*;

import io.realm.RealmResults;

/**
 * Created by Liu Zhengyuan on 2016/5/31.
 */
public class CalendarView  extends FrameLayout {
    private ImageView iv_left;  //进入上个月
    private ImageView iv_right;  //进入下个月
    private TextView tv_date;  //显示当天的日期
    private TextView tv_week;  //显示当天的星期
    private MonthDateView monthDateView;  //月历视图
    private DiaryHelper diaryHelper=new DiaryHelper();

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.calendar, this);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        monthDateView = (MonthDateView) findViewById(R.id.monthDateView);
        tv_date = (TextView) findViewById(R.id.date_text);
        tv_week = (TextView) findViewById(R.id.week_text);
        monthDateView.setTextView(tv_date, tv_week);

        int year = monthDateView.getmSelYear();
        int month = monthDateView.getmSelMonth();
        List<Integer> hasThingList = getMonthList(year, month);  //记录需要标记日期的List，可用来表示当天有日记的日期
        monthDateView.setDaysHasThingList(hasThingList);
        setOnlistener();  //初始化monthDateView各按钮点击事件
    }

    /**
     * 初始化monthDateView各按钮点击事件
     */
    private void setOnlistener(){
        iv_left.setOnClickListener(new View.OnClickListener() {

            @Override  //上个月
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        int year = monthDateView.getmSelYear();
                        int month = monthDateView.getmSelMonth();
                        if (month == 0)
                            monthDateView.setDaysHasThingList(getMonthList(year - 1, 11));
                        else
                            monthDateView.setDaysHasThingList(getMonthList(year, month - 1));
                        monthDateView.onLeftClick();
                    }
                }.start();
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override  //下个月
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        int year = monthDateView.getmSelYear();
                        int month = monthDateView.getmSelMonth();
                        if (month == 11)
                            monthDateView.setDaysHasThingList(getMonthList(year + 1, 0));
                        else
                            monthDateView.setDaysHasThingList(getMonthList(year, month + 1));
                        monthDateView.onRightClick();
                    }
                }.start();
            }
        });

    }

    /**
     * 获取指定月份每天的日记数
     */
    private List<Integer> getMonthList(int year,int month) {
        List<Integer> list = new ArrayList<>();
        DiaryHelper diaryHelper=new DiaryHelper();
        for (int i = 1; i <= getMonthDays(year, month); i++) {  //对每一天
            GregorianCalendar date = new GregorianCalendar();
            date.set(year, month, i);
            RealmResults<Diary> diaries = diaryHelper.getDiariesOfDay(date);
            list.add(diaries.size());
        }
        return list;
    }

    /**
     * 设置日期点击事件
     * @param dataClick
     */
    public void setDateClick(MonthDateView.DateClick dataClick){
        monthDateView.setDateClick(dataClick);
    }

    public void setList(List<Integer> list){
        monthDateView.setDaysHasThingList(list);
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

    /**
     * 返回某年某月有几天
     * @param year
     * @param month
     * @return
     */
    private int getMonthDays(int year, int month) {
        int[] day = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
            day[1] += 1;
        return day[month];
    }
}
