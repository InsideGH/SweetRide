package com.sweetlab.sweetride;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class PreferenceHelper {

    private final Activity mActivity;

    private final String mScreenPrefix;

    private final String mCategoryPrefix;

    private final String mListPrefix;

    private final String mLogPrefix;

    private final String mSamplePrefix;

    private final String mNoSamplePrefix;

    private final String mPickerPrefix;

    private final PreferenceManager mPreferenceManager;

    private final String mCheckBoxTitle;

    private final String mLogMode;

    private final String mLogModeSummary;

    private final String mSampleMode;

    private final String mNoSampleMode;

    private final String mModeSampleKeyPrefix;

    private final String mModeNoSampleKeyPrefix;

    private final String mPickerTitle;

    private final String mPickerDialogTitle;

    public PreferenceHelper(Activity activity, PreferenceManager manager) {
        mPreferenceManager = manager;
        mActivity = activity;
        mScreenPrefix = mActivity.getString(R.string.screen_prefix);
        mCategoryPrefix = mActivity.getString(R.string.category_prefix);
        mListPrefix = mActivity.getString(R.string.list_prefix);
        mLogPrefix = mActivity.getString(R.string.log_prefix);
        mSamplePrefix = mActivity.getString(R.string.mode_sample_prefix);
        mNoSamplePrefix = mActivity.getString(R.string.mode_no_sample_prefix);
        mPickerPrefix = mActivity.getString(R.string.picker_prefix);

        mCheckBoxTitle = mActivity.getString(R.string.checkbox_title);
        mLogMode = mActivity.getString(R.string.log_mode);
        mLogModeSummary = mActivity.getString(R.string.log_mode_summary);

        mSampleMode = mActivity.getString(R.string.sample_mode);
        mNoSampleMode = mActivity.getString(R.string.no_sample_mode);
        mModeSampleKeyPrefix = mActivity.getString(R.string.mode_sample_prefix);
        mModeNoSampleKeyPrefix = mActivity.getString(R.string.mode_no_sample_prefix);

        mPickerTitle = mActivity.getString(R.string.picker_title);
        mPickerDialogTitle = mActivity.getString(R.string.picker_dialog_title);

    }

    public PreferenceScreen createScreen(String name) {
        PreferenceScreen screen = mPreferenceManager.createPreferenceScreen(mActivity);
        screen.setKey(mScreenPrefix + name);
        screen.setTitle(name);
        return screen;
    }

    public PreferenceCategory createCategory(String name) {
        PreferenceCategory category = new PreferenceCategory(mActivity);
        category.setKey(mCategoryPrefix + name);
        category.setTitle(name);
        return category;
    }

    public CheckBoxPreference createCheckBox(String name) {
        CheckBoxPreference checkBox = new CheckBoxPreference(mActivity);
        checkBox.setKey(mLogPrefix + name);
        checkBox.setTitle(mCheckBoxTitle);
        return checkBox;
    }

    public ListPreference createListPreference(String name) {
        final ListPreference listPreference = new ListPreference(mActivity);
        listPreference.setKey(mListPrefix + name);
        listPreference.setTitle(mLogMode);
        listPreference.setSummary(mLogModeSummary);

        String modeSampleKeyValue = mModeSampleKeyPrefix + name;
        String modeNoSampleKeyValue = mModeNoSampleKeyPrefix + name;

        String[] entries = {mSampleMode, mNoSampleMode};
        String[] entryValues = {modeSampleKeyValue, modeNoSampleKeyValue};

        listPreference.setEntries(entries);
        listPreference.setEntryValues(entryValues);
        listPreference.setDefaultValue(modeNoSampleKeyValue);
        listPreference.setSummary(mNoSampleMode);

        String listPrefValue = mPreferenceManager.getSharedPreferences().getString(getListPreferenceKey(name), null);
        String summary = getEntryFromEntryValue(listPreference, listPrefValue);
        if (summary != null) {
            listPreference.setSummary(summary);
        }

        return listPreference;
    }

    public NumberPickerPreference createNumberPicker(String name) {
        NumberPickerPreference picker = new NumberPickerPreference(mActivity);
        picker.setTitle(mPickerTitle);
        picker.setKey(mPickerPrefix + name);
        picker.setDialogTitle(mPickerDialogTitle);
        return picker;
    }

    public void updateListSummary(ListPreference preference, String entryValue) {
        String entryText = getEntryFromEntryValue(preference, entryValue);
        if (entryText != null) {
            preference.setSummary(entryText);
        }
    }

    public void updatePickerEnable(String newEntryValue, String name) {
        Preference picker = findNumberPicker(name);
        if (getListSampleModeEntryValue(name).equals(newEntryValue)) {
            picker.setEnabled(true);
        } else {
            picker.setEnabled(false);
        }
    }

    public String getListPreferenceValue(String name, String def) {
        SharedPreferences sharedPreferences = mPreferenceManager.getSharedPreferences();
        return sharedPreferences.getString(getListPreferenceKey(name), def);
    }

    private String getEntryFromEntryValue(ListPreference preference, String keyValue) {
        if (keyValue != null) {
            CharSequence[] entries = preference.getEntries();
            CharSequence[] entryValues = preference.getEntryValues();

            for (int i = 0; i < entryValues.length; i++) {
                if (keyValue.equals(entryValues[i])) {
                    return (String) entries[i];
                }
            }
        }
        return null;
    }

    private Preference findNumberPicker(String name) {
        return mPreferenceManager.findPreference(getNumberPickerKey(name));
    }

    private String getNumberPickerKey(String name) {
        return mPickerPrefix + name;
    }

    private String getListPreferenceKey(String name) {
        return mListPrefix + name;
    }

    private String getListSampleModeEntryValue(String name) {
        return mModeSampleKeyPrefix + name;
    }
}
