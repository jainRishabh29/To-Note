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
        const val passcode = "passcode"
        const val isFromOtherFragment = "isFromOtherFragment"
    }

    fun saveValue(key: String, value: Int) {
        val editor = prefs?.edit()
        editor?.putInt(key, value)
        editor?.apply()
    }

    fun savePasscodeValue(key: String, value: String) {
        val editor = prefs?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun getValue(key: String): Int? {
        return prefs?.getInt(key, 0)
    }

    fun getPasscodeValue(key: String): String? {
        return prefs?.getString(key, null)
    }

    fun deleteValue(key: String) {
        val editor = prefs?.edit()
        editor?.putString(key, null)
        editor?.apply()
    }
}