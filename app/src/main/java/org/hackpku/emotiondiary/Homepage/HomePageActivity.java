package org.hackpku.emotiondiary.Homepage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.*;

import java.util.*;

import org.hackpku.emotiondiary.MainApplication;
import org.hackpku.emotiondiary.R;
import org.hackpku.emotiondiary.common.Diary.*;

import io.realm.*;

/**
 * Created by Liu Zhengyuan on 2016/6/4.
 */
public class HomePageActivity extends FragmentActivity {

    private CalendarView calendarView;
    private TextView tv_date;
    private ListView lv_diary;
    private DiaryHelper diaryHelper=new DiaryHelper();
    private List<Map<String, Object>> diaryData = new ArrayList<Map<String, Object>>();
    private int currentTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentTheme = ((MainApplication)getApplication()).getThemeId();
        setTheme(currentTheme);
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

    @Override
    protected void onStart() {
        super.onStart();
        if (currentTheme != ((MainApplication)getApplication()).getThemeId()){
            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
        }
    }

    /**
     * 设置日期的点击事件
     */
    private void setDateClick(){
        calendarView.setDateClick(new MonthDateView.DateClick() {
            @Override
            public void onClickOnDate() {
                tv_date.setText(calendarView.getSelMonth() + "月" + calendarView.getSelDay() + "日");
                new Thread() {
                    public void run() {
                        //显示当天日记，这里改成线程
                        DiaryHelper diaryHelper=new DiaryHelper();
                        GregorianCalendar date = new GregorianCalendar();
                        date.set(calendarView.getSelYear(), calendarView.getSelMonth() - 1, calendarView.getSelDay());
                        RealmResults<Diary> diaries = diaryHelper.getDiariesOfDay(date);
                        SetData(diaries);
                        handler.sendEmptyMessage(ID_SET_LIST_VIEW);
                    }
                }.start();
            }
        });
    }

    private  static final int ID_SET_LIST_VIEW = 10086;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case ID_SET_LIST_VIEW:
                    SetListView();
                    break;
            }
        }
    };

    /**
     * 设置显示日记摘要的ListView
     */
    private void SetListView() {
        SimpleAdapter adapter = new SimpleAdapter(this, diaryData, R.layout.diary_outline,
                new String[]{"photo", "emotion", "time", "diary"},
                new int[]{R.id.photo_view, R.id.emotion_view, R.id.time_text, R.id.diary_text});
        adapter.setViewBinder(new ListViewBinder());
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

    private class ListViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data,
                                    String textRepresentation) {
            // TODO Auto-generated method stub
            if((view instanceof RoundImageView) && (data instanceof Bitmap)) {
                RoundImageView imageView = (RoundImageView) view;
                Bitmap bmp = (Bitmap) data;
                imageView.setImageBitmap(bmp);
                return true;
            }
            return false;
        }

    }

    /**
     * 设置今天的日记
     */
    private void setToday() {
        GregorianCalendar date=new  GregorianCalendar();
        Calendar calendar = Calendar.getInstance();  //今天
        date.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
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
            map.put("photo", diary.getSelfie().getImage());

            int emotion = diary.getHappiness();
            if (emotion > 66)
                map.put("emotion", R.drawable.laugh3);
            else if (emotion > 33)
                map.put("emotion", R.drawable.smile3);
            else
                map.put("emotion", R.drawable.sad3);

            Date date = diary.getDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            String time = String.format("%02d", hour) + ":" + String.format("%02d", minute);
            map.put("time", time);

            map.put("diary", diary.getText());
            diaryData.add(map);
        }
    }

}
