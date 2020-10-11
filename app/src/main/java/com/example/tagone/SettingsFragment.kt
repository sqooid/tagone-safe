package com.example.tagone

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.codekidlabs.storagechooser.StorageChooser
import com.kotlinpermissions.KotlinPermissions

class SettingsFragment : PreferenceFragmentCompat() {

    val PREFERENCES_NAME = "preferences"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val manager = preferenceManager
        manager.sharedPreferencesName = PREFERENCES_NAME

        setPreferencesFromResource(R.xml.root_preferences, rootKey)


    }

    @SuppressLint("CommitPrefEdits")
    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        if (preference != null) {
            when (preference.key) {
                "storage_location" -> {
                    activity?.let {
                        KotlinPermissions.with(it)
                            .permissions(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                            .ask()
                    }

                    val chooser = StorageChooser.Builder()
                        .withActivity(activity)
                        .withFragmentManager(activity?.fragmentManager)
                        .withMemoryBar(true)
                        .allowCustomPath(true)
                        .setType(StorageChooser.DIRECTORY_CHOOSER)
                        .actionSave(true)
                        .withPreference(context?.getSharedPreferences(PREFERENCES_NAME, 0))
                        .showHidden(true)
                        .build()
                    chooser.show()
                    chooser.setOnSelectListener {
                        Log.i("test", "Path: $it")
                        preference.summary = it
                    }
                }
            }
        }
        return super.onPreferenceTreeClick(preference)
    }
}