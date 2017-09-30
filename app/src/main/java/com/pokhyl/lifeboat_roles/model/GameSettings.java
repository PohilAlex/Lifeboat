package com.pokhyl.lifeboat_roles.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;


import java.util.List;

@AutoValue
public abstract class GameSettings implements Parcelable {

    public static final int DEF_PLAYER_NUMBER = 6;
    public static final boolean DEF_USE_RANDOM = true;

    public abstract int playerNumber();

    public abstract boolean useRandom();

    public abstract @Nullable List<Person> personList();

    public static Builder builder() {
        return new AutoValue_GameSettings.Builder()
                .playerNumber(DEF_PLAYER_NUMBER)
                .useRandom(DEF_USE_RANDOM);
    }

    @AutoValue.Builder
    public interface Builder {
        Builder playerNumber(int playerNumber);

        Builder useRandom(boolean useRandom);

        Builder personList(List<Person> personList);

        GameSettings build();
    }
}
