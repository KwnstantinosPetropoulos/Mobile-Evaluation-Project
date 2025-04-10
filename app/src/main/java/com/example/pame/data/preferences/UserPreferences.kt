package com.example.pame.data.preferences

import android.content.Context

object UserPreferences {
    private const val PREFS_NAME = "user_prefs"
    private const val KEY_ACCESS_TOKEN = "access_token"

    fun saveAccessToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ACCESS_TOKEN, token)
        editor.apply()
    }

    fun getAccessToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }
}
