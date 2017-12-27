package com.example.translationapplication.util;

/**
 * Created by 보운 on 2017-12-28.
 */

public class DataManager {
    private TranslationType type = TranslationType.SMT;

    private static class Singleton {
        private static final DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance() {
        return Singleton.INSTANCE;
    }

    public TranslationType getType() {
        return type;
    }

    public void setType(TranslationType type) {
        this.type = type;
    }
}
