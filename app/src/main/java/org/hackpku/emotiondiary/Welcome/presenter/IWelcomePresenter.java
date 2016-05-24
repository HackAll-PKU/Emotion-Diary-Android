package org.hackpku.emotiondiary.Welcome.presenter;

/**
 * Created by Archimekai on 5/24/2016.
 */
public interface IWelcomePresenter {
    boolean doLogIn();  // 此处应该修改函数需要的参数，比如face++id之类
    void recordEmotion();
    void enterHomepage();
}

