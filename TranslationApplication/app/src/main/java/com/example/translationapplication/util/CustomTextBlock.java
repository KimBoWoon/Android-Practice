package com.example.translationapplication.util;

import android.graphics.Point;
import android.graphics.Rect;

import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.List;

/**
 * Created by Null on 2017-12-27.
 */

public class CustomTextBlock implements CustomTextBlockInterface {
    private TextBlock textBlock;
    private String translatedText;

    public CustomTextBlock(TextBlock textBlock) {
        this.textBlock = textBlock;
    }

    public String getLanguage() {
        return textBlock.getLanguage();
    }

    public String getValue() {
        return this.textBlock.getValue();
    }

    public Point[] getCornerPoints() {
        return this.textBlock.getCornerPoints();
    }

    public List<? extends Text> getComponents() {
        return this.textBlock.getComponents();
    }

    public Rect getBoundingBox() {
        return this.textBlock.getBoundingBox();
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }
}
