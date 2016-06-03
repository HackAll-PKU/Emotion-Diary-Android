package org.hackpku.emotiondiary.Welcome.view;

/**
 * Created by Archimekai on 5/24/2016.
 */
public interface IWelcomeView {

    void onLogInResult(boolean logInResult, String msg);
    void onRecordEmotion();
    void onEnterHomepage();
    void makeToast(String str);
    void changeBackgroundColorAccordingToSmiling(double smiling);
}
