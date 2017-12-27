package com.example.translationapplication.util;

import android.graphics.Point;
import android.graphics.Rect;

import com.google.android.gms.vision.text.Text;

import java.util.List;

/**
 * Created by 보운 on 2017-12-28.
 */

public interface CustomTextBlockInterface {
    String getLanguage();

    String getValue();

    Point[] getCornerPoints();

    List<? extends Text> getComponents();

    Rect getBoundingBox();

    void setTranslatedText(String translatedText);

    String getTranslatedText();
}
