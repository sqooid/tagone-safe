package com.example.tagone

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.codekidlabs.storagechooser.StorageChooser
import com.example.tagone.util.Constants
import com.kotlinpermissions.KotlinPermissions

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var preferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferences = requireActivity().getSharedPreferences("preferences", 0)

        val manager = preferenceManager
        manager.sharedPreferencesName = Constants.PREFERENCE_NAME

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
                        .showHidden(true)
                        .actionSave(true)
                        .withPreference(preferences)
                        .build()
                    chooser.show()
                    chooser.setOnSelectListener {
                        Log.i("test", "Got path: $it")
                        preference.summary = it
                    }
                }
            }
        }
        return super.onPreferenceTreeClick(preference)
    }
}