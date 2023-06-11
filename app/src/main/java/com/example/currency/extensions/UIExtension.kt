package com.example.currency.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast

inline fun <reified T: Activity> Activity.navigate(func: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.func()
    startActivity(intent)
}

fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(s.toString())
        }
    })
}

fun Context.toast(resourceId: Int) = toast(getString(resourceId))

fun Context.toast(message: CharSequence?) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

