package com.bowoon.unit_test;

/**
 * Created by 보운 on 2016-12-18.
 */

public interface MainContract {
    interface View {
        void initView();
        void setText(String s);
    }

    interface UserAction {
        void sumButtonClick(int val1, int val2);
    }
}
