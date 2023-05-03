package ru.vsu.cs.tp.richfamily.utils

import android.content.Context

class SharedPrefUtils {

    companion object {

        @JvmStatic
        fun saveToken(context: Context, token: String) {
            val sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString("authToken", "Token $token")
                commit()
            }
        }

        @JvmStatic
        fun getToken(context: Context) : String {
            val sharedPref = context.getSharedPreferences(
                "myPrefs",
                Context.MODE_PRIVATE
            )
            return sharedPref.getString("authToken", "").toString()
        }

        fun isEmpty(context: Context) : Boolean {
            val sharedPref = context.getSharedPreferences(
                "myPrefs",
                Context.MODE_PRIVATE
            )
            return !sharedPref.contains("myPref")
        }
    }
}