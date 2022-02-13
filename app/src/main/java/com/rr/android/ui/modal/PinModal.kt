package com.rr.android.ui.modal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rr.android.R
import com.rr.android.databinding.LayoutModalPinBinding
import com.rr.android.ui.viewmodel.SessionVM
import com.rr.android.util.extensions.value
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PinModal(private val listener: PinListener) : BottomSheetDialogFragment() {
    private val sessionVM: SessionVM by viewModels()

    private val binding: LayoutModalPinBinding by lazy {
        LayoutModalPinBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        with(binding) {
            btnConfirm.setOnClickListener {
                when {
                    sessionVM.isValidPin(tvPin.value()) -> onValidPin()
                    else -> Toast.makeText(context, R.string.error_pin, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun onValidPin() {
        dismiss()
        listener.onPinSuccess()
    }

    interface PinListener {
        fun onPinSuccess()
    }

    companion object {
        private const val TAG = "PIN_MODAL"
        fun show(supportFragmentManager: FragmentManager, listener: PinListener) {
            PinModal(listener).show(supportFragmentManager, TAG)
        }
    }
}
