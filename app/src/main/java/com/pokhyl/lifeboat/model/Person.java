package com.pokhyl.lifeboat.model;


public enum Person {

    //don't change names
    KID(3, 9),
    LADY_LAUREN(4, 8),
    SIR_STEPHEN(5, 7),
    FRENCHY(6, 6),
    CAPTAIN(7, 5),
    FIRST_MATE(8, 4);

    public int strength;
    public int survivalPoints;

    Person(int strength, int survivalPoints) {
        this.strength = strength;
        this.survivalPoints = survivalPoints;
    }
}
