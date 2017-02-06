package com.pokhyl.lifeboat;


import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.pokhyl.lifeboat.model.Person;

public class PersonResourceManager {

    @DrawableRes
    public static int getImage(Person person) {
        switch (person) {
            case KID:
                return R.drawable.img_kid;
            case LADY_LAUREN:
                return R.drawable.img_lady_lauren;
            case SIR_STEPHEN:
                return R.drawable.img_sir_stephen;
            case FRENCHY:
                return R.drawable.img_franchy;
            case CAPTAIN:
                return R.drawable.img_captain;
            case FIRST_MATE:
                return R.drawable.img_first_mate;
            default:
                return R.drawable.img_kid;
        }
    }

    @StringRes
    public static int getTitle(Person person) {
        switch (person) {
            case KID:
                return R.string.person_kid;
            case LADY_LAUREN:
                return R.string.person_lady_lauren;
            case SIR_STEPHEN:
                return R.string.person_sir_stephen;
            case FRENCHY:
                return R.string.person_frenchy;
            case CAPTAIN:
                return R.string.person_captain;
            case FIRST_MATE:
                return R.string.person_first_mat;
            default:
                return R.string.person_kid;
        }
    }
}
