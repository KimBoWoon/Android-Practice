package com.practice.android.overlay.service;

/**
 * Created by null on 11/20/17.
 */

public class TranslationModel {
    private String translatedText;
    private String srcLangType;

    public TranslationModel(String translatedText, String srcLangType) {
        this.translatedText = translatedText;
        this.srcLangType = srcLangType;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public String getSrcLangType() {
        return srcLangType;
    }

    public void setSrcLangType(String srcLangType) {
        this.srcLangType = srcLangType;
    }
}
