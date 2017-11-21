package com.practice.android.overlay.service;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by null on 11/21/17.
 */

public class TranslationDeserializer implements JsonDeserializer {
    @Override
    public TranslationModel deserialize(JsonElement je, Type typeOfT, JsonDeserializationContext jdc)
            throws JsonParseException {
        JsonElement json = je.getAsJsonObject().get("message").getAsJsonObject().get("result");
        return new Gson().fromJson(json, TranslationModel.class);
    }
}
