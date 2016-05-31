package org.hackpku.emotiondiary.common.Diary;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by ChenLetian on 16/5/28.
 * Diary的帮助类
 */
public class DiaryHelper {

    public DiaryHelper(Context context) {
        this.context = context;
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        Realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getDefaultInstance();
    }

    Realm realm;
    private Context context;
    private String TAG = "EmotionDiary.DiaryHelper";

    /**
     * 保存日记
     * @param diary 需要被保存的日记
     */
    public void saveDiary(Diary diary) {
        realm.beginTransaction();
        realm.copyToRealm(diary);
        realm.commitTransaction();
        Log.v(TAG, "saveDiary OK");
    }

    /**
     * 更新日记
     * @param transaction 在此transaction里面对某些diary进行操作即可
     */
    public void updateDiary(Realm.Transaction transaction) {
        realm.executeTransaction(transaction);
        Log.v(TAG, "updateDiary OK");
    }

    /**
     * 删除一条日记
     * @param diary 要删除的日记
     */
    public void deleteDiary(final Diary diary) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                diary.deleteFromRealm();
                Log.v(TAG, "deleteDiary OK");
            }
        });
    }

    /**
     * 获取最近一条日记
     * @return 最近一条日记，若无则为null
     */
    public Diary getLastestDiary() {
        RealmResults<Diary> diaries = realm.where(Diary.class).findAllSorted("date", Sort.DESCENDING);
        if (diaries.size() > 0) {
            Diary diary = diaries.first();
            Log.v(TAG, "getLatestDiary: " + diary);
            return diary;
        }
        else {
            Log.v(TAG, "getLatestDiary: there is no diary");
            return null;
        }
    }

    /**
     * 获取某一天的日记
     * @param date 以那一天作为年月日的GregorianCalendar
     * @return 那一天的日记
     */
    public RealmResults<Diary> getDiariesOfDay(GregorianCalendar date) {
        GregorianCalendar thisDay = transformDateToGregorianCalendar(date);
        Date thisDate = thisDay.getTime();
        thisDay.add(Calendar.DAY_OF_MONTH, 1);
        Date nextDate = thisDay.getTime();
        RealmResults<Diary> diaries = realm.where(Diary.class).between("date", thisDate, nextDate).findAllSorted("date", Sort.DESCENDING);
        Log.v(TAG, "getDiariesOfDay: " + diaries);
        return diaries;
    }

    /**
     * 获取过去几天的快乐值
     * @param days 过去几天（若为1即为只有今天）
     * @return Date与Double组成的Map，表示某一天的快乐值为多少
     */
    public TreeMap<Date, Double> getHappinessForTime(int days) {
        GregorianCalendar thisDay = transformDateToGregorianCalendar(new GregorianCalendar());
        thisDay.add(Calendar.DAY_OF_YEAR, 1);
        Date destinationDate = thisDay.getTime();
        thisDay.add(Calendar.DAY_OF_YEAR, -days);
        Date nowQueryDate = thisDay.getTime();
        RealmResults<Diary> diaries = realm.where(Diary.class).between("date", nowQueryDate, destinationDate).findAllSorted("date", Sort.ASCENDING);
        TreeMap<Date, Double> result = new TreeMap<>();
        int i = 0;
        while (nowQueryDate.getTime() < destinationDate.getTime()) {
            int happiness = 0;
            int happinessCount = 0;
            while (i < diaries.size()) {
                Diary diary = diaries.get(i);
                long timeDelta = diary.getDate().getTime() - nowQueryDate.getTime();
                if ((timeDelta >= 0) && (timeDelta < 24*60*60*1000)) {
                    happiness += diary.getHappiness();
                    happinessCount++;
                    i++;
                }
                else {
                    break;
                }
            }
            double averageHappiness = happinessCount == 0 ? 0 : happiness / happinessCount;
            result.put(nowQueryDate, averageHappiness);
            thisDay.add(Calendar.DAY_OF_YEAR, 1);
            nowQueryDate = thisDay.getTime();
        }
        return result;
    }

    /**
     * 将date中的年月日提取出来构造GregorianCalendar对象（舍弃时间信息，仅保留日期信息）
     * @param date GregorianCalendar对象
     * @return 转换好的GregorianCalendar对象
     */
    private GregorianCalendar transformDateToGregorianCalendar(GregorianCalendar date) {
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        return new GregorianCalendar(year, month, day);
    }

    @Override
    protected void finalize() throws Throwable {
        realm.close();
        super.finalize();
    }
}
