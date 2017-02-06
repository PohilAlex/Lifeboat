package com.pokhyl.lifeboat;


import android.os.Bundle;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.pokhyl.lifeboat.model.Person;


public class SettingsFragment extends PreferenceFragmentCompat {


    private String[] playerNumberList = new String[]{"4", "5", "6"};

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_general, rootKey);

        initPlayersNumberPref();
        initPlayersRolePref();
    }

    private void initPlayersNumberPref() {
        ListPreference numberPreferences = (ListPreference) findPreference("player_number");
        numberPreferences.setEntryValues(playerNumberList);
        numberPreferences.setEntries(playerNumberList);
        numberPreferences.setOnPreferenceChangeListener((preference, newValue) -> {
            preference.setSummary(newValue.toString());
            return true;
        });
        numberPreferences.setSummary(numberPreferences.getEntry());
    }

    private void initPlayersRolePref() {
        MultiSelectListPreference rolePreferences = (MultiSelectListPreference) findPreference("player_roles");

        String[] rolesList = Stream.of(Person.values())
                .map(person -> getActivity().getString(PersonResourceManager.getTitle(person)))
                .toArray(value -> new String[value]);

        String rolesSummary = Stream.of(Person.values())
                .map(person -> getActivity().getString(PersonResourceManager.getTitle(person)))
                .collect(Collectors.joining(", "));

        rolePreferences.setEntries(rolesList);
        rolePreferences.setEntryValues(rolesList);
        rolePreferences.setSummary(rolesSummary);
    }
}
