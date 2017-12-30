package com.example.translationapplication.util;

/**
 * Created by 보운 on 2017-12-28.
 */

public class DataManager {
    private TranslationType type = TranslationType.SMT;
    private String source = "en";
    private String target = "ko";

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
