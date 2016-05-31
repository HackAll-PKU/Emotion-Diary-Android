package org.hackpku.emotiondiary.Welcome.presenter;

import android.content.Intent;

/**
 * Created by Archimekai on 5/24/2016.
 */
public interface IWelcomePresenter {
    void doLogIn();  // 此处应该修改函数需要的参数，比如face++id之类
    void recordEmotion();
    void enterHomepage();

    void onActivityResult(int requestCode, int resultCode, Intent data);
    void makeAlertDialog(String title, String message);
}

