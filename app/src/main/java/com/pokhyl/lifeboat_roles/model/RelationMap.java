package com.pokhyl.lifeboat_roles.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class RelationMap {

    @JsonProperty("relationMap")
    private Map<Person, PersonRelation> map;

    public RelationMap() {
    }

    public RelationMap(Map<Person, PersonRelation> map) {
        this.map = map;
    }

    public Map<Person, PersonRelation> getMap() {
        return map;
    }

    public void setMap(Map<Person, PersonRelation> map) {
        this.map = map;
    }
}
