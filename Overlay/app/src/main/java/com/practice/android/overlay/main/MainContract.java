package com.practice.android.overlay.main;

/**
 * Created by null on 11/21/17.
 */

public interface MainContract {
    interface View {
        void initView();
    }

    interface UserAction {
        void translationClick();
    }
}
