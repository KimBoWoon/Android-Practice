package com.bowoon.android.aac_practice;

import org.joda.time.DateTime;

import androidx.room.TypeConverter;

/**
 * room에 저장된 날짜 데이터를 보기 쉽게 바꿔주는 클래스
 */
public class DateConverter {
    @TypeConverter
    public static DateTime toDate(Long timestamp) {
        return timestamp == null ? null : new DateTime(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(DateTime date) {
        return date == null ? null : date.getMillis();
    }
}
