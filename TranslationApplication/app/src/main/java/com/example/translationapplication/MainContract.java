package com.example.translationapplication;

/**
 * Created by 보운 on 2017-12-19.
 */

public interface MainContract {
    interface View {
        void initView();
        void setText(String s);
    }
    interface UserAction {
        void requestBtnClick(TranslationType transType, String s);
    }
}
