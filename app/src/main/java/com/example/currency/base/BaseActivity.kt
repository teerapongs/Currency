package com.example.currency.base

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.example.currency.data.UserData
import com.example.currency.extensions.toast
import com.google.gson.GsonBuilder

open class BaseActivity: AppCompatActivity() {
    lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userData = UserData(
            pref = getSharedPreferences(UserData.PREFERENCES_NAME, MODE_PRIVATE),
            gson = GsonBuilder().create()
        )
    }

    fun uiShowError(message: String?) {
        toast(message)
    }

    fun uiShowError(@StringRes message: Int) {
        toast(message)
    }
}