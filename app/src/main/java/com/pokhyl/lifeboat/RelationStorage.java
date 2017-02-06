package com.pokhyl.lifeboat;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokhyl.lifeboat.model.Person;
import com.pokhyl.lifeboat.model.PersonRelation;
import com.pokhyl.lifeboat.model.RelationMap;

import java.io.IOException;
import java.util.Map;

public class RelationStorage {

    private static final String PREFERENCE_NAME = "pref";
    private static final String KEY_RELATION = "relation";

    SharedPreferences preferences;
    ObjectMapper mapper;

    public RelationStorage(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public Map<Person, PersonRelation> getRelations() {
        try {
            String relation = preferences.getString(KEY_RELATION, "");
            RelationMap relationMap = mapper.readValue(relation, RelationMap.class);
            return relationMap.getMap();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Error", "Data can not read");
            return null;
        }
    }

    public void storeRelation(Map<Person, PersonRelation> map) {
        try {
            RelationMap relationMap = new RelationMap(map);
            String data = mapper.writeValueAsString(relationMap);
            preferences.edit().putString(KEY_RELATION, data).apply();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Log.e("Error", "Data is not save");
        }
    }




}
