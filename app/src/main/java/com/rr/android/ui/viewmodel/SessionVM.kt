package com.rr.android.ui.viewmodel

import com.rr.android.ui.base.BaseViewModel
import com.rr.android.util.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SessionVM @Inject constructor(
    private val prefs: Prefs
) : BaseViewModel() {

    val isFingerPrintEnable: Boolean
        get() = prefs.useFingerPrint

    val pinCode: String
        get() = prefs.accessPin

    fun saveNewPin(newPin: String) {
        prefs.accessPin = newPin
    }

    fun isNewPinValid(newPin: String): Boolean = newPin.length >= MIN_PIN_SIZE

    val isPinCodeAvailable: Boolean
        get() = pinCode.isNotEmpty()

    fun isValidPin(pin: String): Boolean = pin == pinCode

    fun enableFingerPrint(enable: Boolean) {
        prefs.useFingerPrint = enable
    }

    companion object {
        private const val MIN_PIN_SIZE = 4
    }
}
