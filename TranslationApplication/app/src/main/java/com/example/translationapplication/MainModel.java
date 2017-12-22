package com.example.translationapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.google.common.base.Objects;

/**
 * Created by 보운 on 2017-12-19.
 */

public class MainModel {
    @SerializedName("translatedText")
    private String translatedText;
    @SerializedName("srcLangType")
    private String srcLangType;

    @Expose
    private String srcText;

    @Expose
    private String transType;

    public MainModel() {
    }

    public MainModel(String translatedText, String srcLangType) {
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

    public String getSrcText() {
        return srcText;
    }

    public void setSrcText(String srcText) {
        this.srcText = srcText;
    }

    public TranslationType getTransTypeEnum() {
        return TranslationType.valueOf(transType);
    }

    public void setTransTypeEnum(TranslationType type) {
        this.transType = type.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MainModel transData = (MainModel) o;
        return Objects.equal(translatedText, transData.translatedText) &&
                Objects.equal(srcLangType, transData.srcLangType) &&
                Objects.equal(srcText, transData.srcText);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(translatedText, srcLangType, srcText);
    }
}
