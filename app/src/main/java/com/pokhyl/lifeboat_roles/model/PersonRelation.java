package com.pokhyl.lifeboat_roles.model;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonDeserialize(builder = AutoValue_PersonRelation.Builder.class)
public abstract class PersonRelation implements Parcelable{

    @JsonProperty("friend")
    public abstract @NonNull Person friend();

    @JsonProperty("enemy")
    public abstract @NonNull Person enemy();

    public static PersonRelation create( Person friend,
                                         Person enemy) {
        return builder().friend(friend).enemy(enemy).build(); // new AutoValue_PersonRelation(friend, enemy);
    }

    @NonNull
    public static Builder builder() {
        return new AutoValue_PersonRelation.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        @JsonProperty("friend")
        public abstract Builder friend(@NonNull Person friend);

        @JsonProperty("enemy")
        public abstract Builder enemy(@NonNull Person enemy);

        public abstract PersonRelation build();
    }
}
