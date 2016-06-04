package org.hackpku.emotiondiary.Homepage;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.*;

import java.util.*;

import org.hackpku.emotiondiary.common.Diary.*;

import io.realm.*;

/**
 * Created by Liu Zhengyuan on 2016/6/4.
 */
public class HomePageActivity extends FragmentActivity {

    private CalendarView calendarView;
    private TextView tv_date;
    private ListView lv_diary;
    private DiaryHelper diaryHelper=new DiaryHelper(this);
    private List<Map<String, Object>> diaryData = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        tv_date = (TextView) findViewById(R.id.date_text_view);
        lv_diary=(ListView)findViewById(R.id.diary_listView);

        Calendar calendar = Calendar.getInstance();
        tv_date.setText((calendar.get(Calendar.MONTH) + 1) + "月"+calendar.get(Calendar.DATE)+"日");

        setDateClick();
        setToday();
    }

    /**
     * 设置日期的点击事件
     */
    private void setDateClick(){
        calendarView.setDateClick(new MonthDateView.DateClick() {
            @Override
            public void onClickOnDate() {
                tv_date.setText(calendarView.getSelMonth() + "月" + calendarView.getSelDay() + "日");
                //显示当天日记
                GregorianCalendar date=new  GregorianCalendar();
                date.set(calendarView.getSelYear(),calendarView.getSelMonth(),calendarView.getSelDay());
                RealmResults<Diary> diaries= diaryHelper.getDiariesOfDay(date);
                SetData(diaries);
                SetListView();
            }
        });
    }

    /**
     * 设置显示日记摘要的ListView
     */
    private void SetListView() {
        SimpleAdapter adapter = new SimpleAdapter(this, diaryData, R.layout.diary_outline,
                new String[]{"photo", "emotion", "time", "diary"},
                new int[]{R.id.photo_view, R.id.emotion_view, R.id.time_text, R.id.diary_text});
        lv_diary.setAdapter(adapter);

         //TODO 在这里添加ListViewItem的点击事件，应该是显示详细的日记
        AdapterView.OnItemClickListener listViewListener=new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplication(), adapterView.toString()+"\n"+view.toString()+"\n"+String.valueOf(i)+"\n"+String.valueOf(l), Toast.LENGTH_SHORT).show();
            }
        };
        lv_diary.setOnItemClickListener(listViewListener);
    }

    /**
     * 设置今天的日记
     */
    private void setToday() {
        GregorianCalendar date=new  GregorianCalendar();
        Calendar calendar = Calendar.getInstance();  //今天
        date.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));
        RealmResults<Diary> diaries= diaryHelper.getDiariesOfDay(date);
        SetData(diaries);
        SetListView();
    }

    /**
     * 根据日记集合设置ListView所需map
     * @param diaries 日记集合
     */
    private void SetData(RealmResults<Diary> diaries){
        diaryData = new ArrayList<Map<String, Object>>();
        for(int i=0;i<diaries.size();i++) {
            Diary diary = diaries.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("photo", diary.getSelfie());

            int emotion = diary.getHappiness();
            if (emotion>66)
                map.put("emotion", R.drawable.laugh3);
            else if (emotion>33)
                map.put("emotion", R.drawable.smile3);
            else
                map.put("emotion", R.drawable.sad3);

            Date date = diary.getDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            String time = hour + ":" + minute;
            map.put("time", time);

            map.put("diary", diary.getText());
        }
    }

    /**
     * 临时，展示用
     * @return
     */
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("photo", R.drawable.portrait);
        map.put("emotion", R.drawable.laugh3);
        map.put("time", "7:00");
        map.put("diary", "我今天心情很好");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("photo", R.drawable.portrait);
        map.put("emotion", R.drawable.smile3);
        map.put("time", "9:00");
        map.put("diary", "我今天心情不错");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("photo", R.drawable.portrait);
        map.put("emotion", R.drawable.sad3);
        map.put("time", "11:00");
        map.put("diary", "我今天心情一般");
        list.add(map);

        return list;
    }

}
