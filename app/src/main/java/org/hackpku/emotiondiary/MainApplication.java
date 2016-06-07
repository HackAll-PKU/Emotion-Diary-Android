package org.hackpku.emotiondiary;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by HuShunxin on 16/6/4.
 */
public class MainApplication extends Application {

    private double smiling = 0;

    public static final int THEME_BLUE = R.style.AppTheme;
    public static final int THEME_YELLOW = R.style.AppTheme_Yellow;
    public static final int THEME_ORANGE = R.style.AppTheme_Orange;

    /**
     * 获取当前心情值（0~100）
     */
    public double getSmiling() {
        return smiling;
    }

    /**
     * 设置心情值（0~100），修改时务必调用
     * @param smiling 心情值
     */
    public void setSmiling(double smiling) {
        this.smiling = smiling;
    }

    /**
     * 获取当前心情值对应的Theme ID
     * @return Theme ID
     */
    public int getThemeId(){
        if (smiling < 33) return THEME_BLUE;
        else if (smiling > 66) return THEME_ORANGE;
        else return THEME_YELLOW;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
