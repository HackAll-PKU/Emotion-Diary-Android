package org.hackpku.emotiondiary.Homepage;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import android.widget.*;

import org.hackpku.emotiondiary.MainApplication;
import org.hackpku.emotiondiary.R;

/**
 * Created by Liu Zhengyuan on 2016/5/30.
 */
public class TitleView extends FrameLayout {
    private Button btnTimeline;
    private RelativeLayout layout;
    private TextView titleText;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);
        titleText = (TextView) findViewById(R.id.title_text);
        btnTimeline=(Button)findViewById(R.id.button_timeline);
        layout=(RelativeLayout)findViewById(R.id.layout);

        int color;
        double smiling = ((MainApplication)((Activity)context).getApplication()).getSmiling();
        if (smiling < 33) color = getResources().getColor(R.color.themeColorBlue);
        else if (smiling > 66) color = getResources().getColor(R.color.themeColorOrange);
        else color = getResources().getColor(R.color.themeColorYellow);
        layout.setBackgroundColor(color);


        btnTimeline.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 时间线按钮点击事件，呼出时间线界面
            }
        });

    }

}