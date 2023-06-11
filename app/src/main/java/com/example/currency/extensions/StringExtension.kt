package com.example.currency.extensions

fun String.isFloat() = this.toFloatOrNull()?.let { true } ?: false