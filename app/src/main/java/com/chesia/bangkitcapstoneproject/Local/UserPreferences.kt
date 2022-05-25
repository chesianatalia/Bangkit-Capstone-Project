package com.chesia.bangkitcapstoneproject.Local

import android.content.Context

class UserPreferences (context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val editor = preferences.edit()

    fun setToken(value: String) {
        editor.putString(TOKEN, value)
        editor.apply()
    }

    fun getToken(): String {
        val token: String =
            if (preferences.getString(TOKEN, "") != null) preferences.getString(TOKEN, "")!! else ""
        return token

    }

    fun clearPreference() {
        editor.clear()
        editor.apply()

    }

    companion object{
        private const val PREFS_NAME = "user_pref"
        private const val TOKEN = "token"
    }
}