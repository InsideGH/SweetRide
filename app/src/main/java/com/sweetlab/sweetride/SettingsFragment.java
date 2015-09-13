package com.sweetlab.sweetride;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.sweetlab.sweetride.logger.ControllableLog;
import com.sweetlab.sweetride.logger.LogCategory;
import com.sweetlab.sweetride.logger.Logger;

import java.util.List;

public class SettingsFragment extends PreferenceFragment {
    private PreferenceHelper mPreferenceHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceHelper = new PreferenceHelper(getActivity(), getPreferenceManager());
        addPreferencesFromResource(R.xml.preference_screen);
        addPreferencesFromLogger(getPreferenceScreen());
    }

    private void addPreferencesFromLogger(PreferenceScreen root) {
        List<LogCategory> logCategories = Logger.getCategories();
        for (LogCategory logCategory : logCategories) {
            /**
             * Place the 'log category' inside a separate preference screen.
             */
            PreferenceScreen screen = mPreferenceHelper.createScreen(logCategory.getName());
            root.addPreference(screen);

            List<ControllableLog> logs = logCategory.getLogs();
            for (ControllableLog log : logs) {
                final String name = log.getMessageTag();
                /**
                 * Create a preference category in the screen (headline).
                 */
                PreferenceCategory category = mPreferenceHelper.createCategory(name);
                screen.addPreference(category);

                /**
                 * Create a checkbox preference to enable/disable the log.
                 */
                CheckBoxPreference checkBox = mPreferenceHelper.createCheckBox(name);
                category.addPreference(checkBox);

                /**
                 * Create a mode selector preference (list in a dialog).
                 */
                ListPreference listPreference = mPreferenceHelper.createListPreference(name);
                category.addPreference(listPreference);
                listPreference.setDependency(checkBox.getKey());
                listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        String newEntryValue = (String) newValue;
                        mPreferenceHelper.updateListSummary((ListPreference) preference, newEntryValue);
                        mPreferenceHelper.updatePickerEnable(newEntryValue, name);
                        return true;
                    }
                });

                /**
                 * Create a number picker preference to set log sample rate.
                 */
                NumberPickerPreference picker = mPreferenceHelper.createNumberPicker(name);
                category.addPreference(picker);
                picker.setDependency(checkBox.getKey());
                String sampleModeKeyValue = mPreferenceHelper.getListPreferenceValue(name, null);
                mPreferenceHelper.updatePickerEnable(sampleModeKeyValue, name);
                picker.setSummary(String.valueOf(picker.getValue()) + " ms");
                picker.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        preference.setSummary(newValue.toString() + " ms");
                        return true;
                    }
                });
            }
        }
    }
}