package com.pokhyl.lifeboat.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.ArraySet;
import android.support.v7.preference.PreferenceManager;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.pokhyl.lifeboat.R;
import com.pokhyl.lifeboat.model.GameSettings;
import com.pokhyl.lifeboat.model.Person;
import com.pokhyl.lifeboat.ui.ShowcaseMonitor.AppTourProgress;

import static com.pokhyl.lifeboat.model.GameSettings.DEF_PLAYER_NUMBER;
import static com.pokhyl.lifeboat.model.GameSettings.DEF_USE_RANDOM;

public class SettingsStorage {

    private final String APP_TOUR_PROGRESS = "APP_TOUR_PROGRESS";

    private final SharedPreferences preferences;
    private final Context context;

    public SettingsStorage(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public GameSettings getGameSettings() {
        String playerNumberKey = context.getString(R.string.pref_id_player_number);
        String useRandomKey = context.getString(R.string.pref_id_use_random_role);
        String rolesKey = context.getString(R.string.pref_id_player_roles);

        return GameSettings.builder()
                .playerNumber(Integer.parseInt(preferences.getString(playerNumberKey, String.valueOf(DEF_PLAYER_NUMBER))))
                .useRandom(preferences.getBoolean(useRandomKey, DEF_USE_RANDOM))
                .personList(Stream.of(preferences.getStringSet(rolesKey, new ArraySet<>()))
                        .map(Person::valueOf)
                        .collect(Collectors.toList()))
                .build();
    }

    public void setAppTourProgress(AppTourProgress appTourProgress) {
        preferences.edit()
                .putString(APP_TOUR_PROGRESS, appTourProgress.name())
                .apply();
    }

    public AppTourProgress getAppTourProgress() {
        String progressTitle =  preferences.getString(APP_TOUR_PROGRESS, null);
        if (progressTitle != null) {
            return AppTourProgress.valueOf(progressTitle);
        } else {
            return AppTourProgress.NOT_SHOW_YET;
        }
    }

}
