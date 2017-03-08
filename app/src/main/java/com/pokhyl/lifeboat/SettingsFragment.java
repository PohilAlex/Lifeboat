package com.pokhyl.lifeboat;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.auto.value.AutoValue;
import com.pokhyl.lifeboat.model.Person;

import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String EXTRA_GAME_SETTING = "EXTRA_GAME_SETTING";

    private String[] playerNumberList = new String[]{"4", "5", "6"};
    private ListPreference numberPreferences;
    private MultiSelectListPreference rolePreferences;
    private SwitchPreferenceCompat random_role_preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_general, rootKey);

        numberPreferences = (ListPreference) findPreference("player_number");
        random_role_preference = (SwitchPreferenceCompat) findPreference("use_random_role");
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
                if (isConfigValid()) {
                    showGenerateNewGameDialog();
                } else {
                    Toast.makeText(getActivity(), R.string.settings_wrong_setup_msg, Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isConfigValid() {
        return random_role_preference.isChecked()
                || Integer.parseInt(numberPreferences.getValue()) == rolePreferences.getValues().size();
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
        random_role_preference.setOnPreferenceChangeListener((preference1, newValue) -> {
            Boolean useRandom = (Boolean) newValue;
            rolePreferences.setEnabled(!useRandom);
            return true;
        });
        rolePreferences.setEnabled(!random_role_preference.isChecked());
    }

    private void initPlayersRolePref() {
        String[] rolesTitles = Stream.of(Person.values())
                .map(person -> getString(PersonResourceManager.getTitle(person)))
                .toArray(String[]::new);

        String[] rolesValues = Stream.of(Person.values())
                .map(Enum::toString)
                .toArray(String[]::new);

        rolePreferences.setEntries(rolesTitles);
        rolePreferences.setEntryValues(rolesValues);
        rolePreferences.setSummary(getRolePreferenceSummary(rolePreferences.getValues()));
        rolePreferences.setOnPreferenceChangeListener((preference, newValue) -> {
            Set<String> value = (Set<String>) newValue;
            int playerAmount = Integer.parseInt(numberPreferences.getValue());
            if (value.size() != playerAmount) {
                Toast.makeText(getActivity(), getString(R.string.incorrect_person_selected, playerAmount), Toast.LENGTH_LONG).show();
                return false;
            }
            rolePreferences.setSummary(getRolePreferenceSummary(value));
            return true;
        });
    }

    private String getRolePreferenceSummary(Set<String> values) {
        return Stream.of(values)
                .map(s -> {
                    int position = Arrays.asList(rolePreferences.getEntryValues()).indexOf(s);
                    return rolePreferences.getEntries()[position];
                })
                .collect(Collectors.joining(",  "));
    }

    private void showGenerateNewGameDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.settings_new_game_dialog)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    Intent data = new Intent();
                    data.putExtra(EXTRA_GAME_SETTING, createGameSettings());
                    getActivity().setResult(Activity.RESULT_OK, data);
                    getActivity().finish();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {})
                .create()
                .show();
    }

    private GameSettings createGameSettings() {
        return GameSettings.builder()
                .playerNumber(Integer.parseInt(numberPreferences.getValue()))
                .useRandom(random_role_preference.isChecked())
                .personList(Stream.of(rolePreferences.getValues())
                        .map(Person::valueOf)
                        .collect(Collectors.toList()))
                .build();
    }

    @AutoValue
    public abstract static class GameSettings implements Parcelable {

        public abstract int playerNumber();
        public abstract boolean useRandom();
        public abstract List<Person> personList();

        public static Builder builder() {
            return new AutoValue_SettingsFragment_GameSettings.Builder();
        }

        @AutoValue.Builder
        public interface Builder {
            Builder playerNumber(int playerNumber);
            Builder useRandom(boolean useRandom);
            Builder personList(List<Person> personList);
            GameSettings build();
        }
    }
}
