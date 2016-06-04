package org.hackpku.emotiondiary.Homepage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import android.widget.*;

/**
 * Created by Liu Zhengyuan on 2016/5/30.
 */
public class TitleView extends FrameLayout {
    private Button btnCamera;
    private Button btnTimeline;
    private Button btnUserCenter;
    private TextView titleText;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);
        titleText = (TextView) findViewById(R.id.title_text);
        btnCamera = (Button) findViewById(R.id.button_camera);
        btnTimeline=(Button)findViewById(R.id.button_timeline);
        btnUserCenter=(Button)findViewById(R.id.button_usercenter);

        btnCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 相机按钮点击事件，呼出相机
            }
        });

        btnTimeline.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 时间线按钮点击事件，呼出时间线界面
            }
        });

        btnUserCenter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 用户中心按钮点击事件，呼出用户中心界面
            }
        });
    }

}