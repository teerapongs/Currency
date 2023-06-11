package com.example.currency.data

import android.content.SharedPreferences
import com.example.currency.model.Currency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class UserData(
    private val pref: SharedPreferences,
    private val gson: Gson
) {

    fun setHistoryCurrencyMap(map: Map<String?, Currency?>) =
        pref.edit().putString(KEY_HISTORY_CURRENCY, gson.toJson(map)).apply()

    fun getHistoryCurrencyMap(): Map<String?, Currency?>? {
        val jsonString: String? = pref.getString(KEY_HISTORY_CURRENCY, JSONObject().toString())
        jsonString?.let {
            return when(it.isEmpty()) {
                true -> return null
                else -> {
                    val typeList = object : TypeToken<Map<String?, Currency?>>() {}.type
                    gson.fromJson(it, typeList)
                }
            }
        }?. run { return null }
        return null
    }

    companion object {
        const val PREFERENCES_NAME = "user_data"

        private const val KEY_HISTORY_CURRENCY = "key_history_currency"
    }
}