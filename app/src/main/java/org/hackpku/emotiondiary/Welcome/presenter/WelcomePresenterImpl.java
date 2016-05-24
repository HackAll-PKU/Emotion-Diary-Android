package org.hackpku.emotiondiary.Welcome.presenter;

import org.hackpku.emotiondiary.Welcome.view.IWelcomeView;

/**
 * Created by Archimekai on 5/24/2016.
 * Presenter是MVP模式的核心
 */
public class WelcomePresenterImpl implements IWelcomePresenter{
    IWelcomeView welcomeView;  // presenter通过view来操作activity的表现

    public WelcomePresenterImpl(IWelcomeView welcomeView){
        this.welcomeView = welcomeView;
    }

    @Override
    public boolean doLogIn(){
        // 此处实现登陆逻辑
        boolean logInResult = true;
        welcomeView.onLogInResult(logInResult);
        return logInResult;
    }


    @Override
    public void recordEmotion() {
        // 没有业务逻辑，直接调用welcomeView中的对应方法
        welcomeView.onRecordEmotion();
    }

    @Override
    public void enterHomepage() {
        welcomeView.onEnterHomepage();
    }


}
