package com.example.tonote.util

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.example.tonote.R

class LocalKeyStorage(context: Context) {
    private var prefs: SharedPreferences? =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val sortNumber = "SortNumber"
    }

    fun saveValue(key: String, value: Int) {
        val editor = prefs?.edit()
        editor?.putInt(key, value)
        editor?.apply()
    }

    fun getValue(key: String): Int? {
        return prefs?.getInt(key,0)
    }

    fun deleteValue(key: String) {
        val editor = prefs?.edit()
        editor?.putString(key, null)
        editor?.apply()
    }
}