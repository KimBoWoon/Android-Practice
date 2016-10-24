package com.kimbowoon.android_practice;

/**
 * Created by 보운 on 2016-10-23.
 */

public interface MainContract {
    interface View {
        void setText(String s);
    }

    interface UserAction {
        void sumButtonClick(int val1, int val2);
    }
}
