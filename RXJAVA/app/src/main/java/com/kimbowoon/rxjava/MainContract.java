package com.kimbowoon.rxjava;

/**
 * Created by 보운 on 2016-11-05.
 */

public interface MainContract {
    interface View {
        void init();
        void setText(String s);
    }

    interface UserAction {
        void sumButtonClicked(int val1, int val2);
    }
}
