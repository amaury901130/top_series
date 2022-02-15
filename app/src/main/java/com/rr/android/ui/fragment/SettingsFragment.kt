package com.rr.android.ui.fragment

import android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.rr.android.R
import com.rr.android.secure.Finger
import com.rr.android.ui.viewmodel.SessionVM
import com.rr.android.util.STRING_EMPTY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val finger: Finger by lazy {
        Finger(
            requireContext(),
            mapOf(
                Pair(
                    FINGERPRINT_ERROR_HW_UNAVAILABLE,
                    getString(R.string.error_override_hw_unavailable)
                )
            )
        )
    }

    private val sessionVM: SessionVM by viewModels()

    private var fingerprintsEnabled: Boolean = false

    private val fingerPrintKey: String
        get() = getString(R.string.finger_print_key)
    private val enablePinKey: String
        get() = getString(R.string.enable_pin_key)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        fingerprintsEnabled = finger.hasFingerprintEnrolled()
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference is SwitchPreferenceCompat) {
            when (preference.key) {
                fingerPrintKey -> {
                    sessionVM.enableFingerPrint(preference.isChecked)
                    if (preference.isChecked) {
                        if (!fingerprintsEnabled) {
                            Toast.makeText(
                                requireContext(),
                                R.string.not_finger_print,
                                Toast.LENGTH_LONG
                            ).show()
                            sessionVM.enableFingerPrint(false)
                        }
                    }
                }
                enablePinKey -> {
                    if (preference.isChecked) {
                        setupNewPin()
                    } else {
                        sessionVM.saveNewPin(STRING_EMPTY)
                    }
                }
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun setupNewPin() {
        // TODO: setUpNewPin
        // USE THIS PIN TEMPORALLY
        sessionVM.saveNewPin("0000")
    }
}
