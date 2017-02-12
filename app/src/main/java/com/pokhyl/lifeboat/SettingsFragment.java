package com.pokhyl.lifeboat;


import android.os.Bundle;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.pokhyl.lifeboat.model.Person;

import java.util.Set;


public class SettingsFragment extends PreferenceFragmentCompat {


    private String[] playerNumberList = new String[]{"4", "5", "6"};
    private ListPreference numberPreferences;
    private MultiSelectListPreference rolePreferences;
    private SwitchPreferenceCompat switch_preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_general, rootKey);

        numberPreferences = (ListPreference) findPreference("player_number");
        switch_preference = (SwitchPreferenceCompat) findPreference("use_random_role");
        rolePreferences = (MultiSelectListPreference) findPreference("player_roles");

        initPlayersNumberPref();
        initUseRandomPref();
        initPlayersRolePref();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_apply_config :
                //TODO make check
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPlayersNumberPref() {
        numberPreferences.setEntryValues(playerNumberList);
        numberPreferences.setEntries(playerNumberList);
        numberPreferences.setOnPreferenceChangeListener((preference, newValue) -> {
            preference.setSummary(newValue.toString());
            return true;
        });
        numberPreferences.setSummary(numberPreferences.getEntry());
    }

    private void initUseRandomPref() {
        switch_preference.setOnPreferenceChangeListener((preference1, newValue) -> {
            Boolean useRandom = (Boolean) newValue;
            rolePreferences.setEnabled(!useRandom);
            return true;
        });
        rolePreferences.setEnabled(!switch_preference.isChecked());
    }

    private void initPlayersRolePref() {
        String[] rolesList = Stream.of(Person.values())
                .map(person -> getString(PersonResourceManager.getTitle(person)))
                .toArray(String[]::new);

        String rolesSummary = Stream.of(rolePreferences.getValues()).collect(Collectors.joining(",  "));
        rolePreferences.setEntries(rolesList);
        rolePreferences.setEntryValues(rolesList);
        rolePreferences.setSummary(rolesSummary);
        rolePreferences.setOnPreferenceChangeListener((preference, newValue) -> {
            Set<String> value = (Set<String>) newValue;
            int playerAmount = Integer.parseInt(numberPreferences.getValue());
            if (value.size() != playerAmount) {
                Toast.makeText(getActivity(), getString(R.string.incorrect_person_selected, playerAmount), Toast.LENGTH_LONG).show();
                return false;
            }
            String rolesSummary1 = Stream.of(value).collect(Collectors.joining(",  "));
            rolePreferences.setSummary(rolesSummary1);
            return true;
        });
    }
}
