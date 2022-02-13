package com.rr.android.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE
import android.os.Bundle
import androidx.activity.viewModels
import com.rr.android.R
import com.rr.android.databinding.ActivitySplashBinding
import com.rr.android.secure.Finger
import com.rr.android.secure.FingerListener
import com.rr.android.ui.base.BaseActivity
import com.rr.android.ui.modal.PinModal
import com.rr.android.ui.viewmodel.SessionVM
import com.rr.android.util.LoadingDialogUtil.showError
import com.rr.android.util.extensions.runAfterInactivityPeriod

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity(), FingerListener, PinModal.PinListener {
    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    private val finger: Finger by lazy {
        Finger(
            this,
            mapOf(
                Pair(
                    FINGERPRINT_ERROR_HW_UNAVAILABLE,
                    getString(R.string.error_override_hw_unavailable)
                )
            )
        ).also {
            it.subscribe(this)
        }
    }

    private val sessionVM: SessionVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkSecurity()
    }

    private fun checkSecurity() {
        with(sessionVM) {
            when {
                isFingerPrintEnable -> checkFingerPrint()
                isPinCodeAvailable -> PinModal.show(supportFragmentManager, this@SplashActivity)
                else -> initApp()
            }
        }
    }

    private fun checkFingerPrint() {
        val fingerprintsEnabled = finger.hasFingerprintEnrolled()
        if (!fingerprintsEnabled) {
            initApp()
        } else {
            finger.showDialog(
                this,
                Finger.DialogStrings(title = getString(R.string.text_fingerprint))
            )
        }
    }

    private fun initApp() {
        binding.root.runAfterInactivityPeriod(
            {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            },
            SPLASH_SCREEN_DURATION
        )
    }

    companion object {
        const val SPLASH_SCREEN_DURATION = 1000L
    }

    override fun onFingerprintAuthenticationSuccess() {
        initApp()
    }

    override fun onFingerprintAuthenticationFailure(errorMessage: String, errorCode: Int) {
        if (errorCode == androidx.biometric.BiometricPrompt.ERROR_NO_BIOMETRICS) {
            initApp()
        } else {
            showError(errorMessage, this)
        }
    }

    override fun onPinSuccess() {
        initApp()
    }
}
