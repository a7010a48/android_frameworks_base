/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.android.systemui.tuner;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.ContentResolver;
import android.provider.Settings;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.preference.PreferenceScreen;
import com.android.systemui.R;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

public class StatusbarItems extends PreferenceFragment {

    private static final String NFC_KEY = "nfc";

    private StatusBarSwitch mNfcSwitch;
    private static final String KEY_STATUS_BAR_LOGO = "status_bar_logo";

    private SwitchPreference mShowDOTLogo;

     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
 
         PreferenceScreen prefSet = getPreferenceScreen();
 
         final ContentResolver resolver = getActivity().getContentResolver();

        mShowDOTLogo = (SwitchPreference) findPreference(KEY_STATUS_BAR_LOGO);
        mShowDOTLogo.setChecked((Settings.System.getInt(resolver,
                Settings.System.STATUS_BAR_LOGO, 0) == 1));
     }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.statusbar_items);
        final PreferenceScreen prefScreen = getPreferenceScreen();
        final PackageManager pm = getActivity().getApplicationContext().getPackageManager();

        mNfcSwitch = (StatusBarSwitch) findPreference(NFC_KEY);
        final boolean isNfcAvailable = pm.hasSystemFeature(PackageManager.FEATURE_NFC);

        if (!isNfcAvailable) {
            prefScreen.removePreference(mNfcSwitch);
        }
    }

     @Override
     public boolean onPreferenceTreeClick(Preference preference) {
        if  (preference == mShowDOTLogo) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_LOGO, checked ? 1:0);
            return true;
         }
         return super.onPreferenceTreeClick(preference);
     }
}
