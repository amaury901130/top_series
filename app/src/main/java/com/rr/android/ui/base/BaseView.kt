package com.rr.android.ui.base

interface BaseView {

    fun showProgress()

    fun hideProgress()

    fun showError(message: String?)
}
