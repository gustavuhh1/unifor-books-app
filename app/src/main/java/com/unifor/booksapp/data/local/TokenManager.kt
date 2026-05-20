package com.unifor.booksapp.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit

class TokenManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit {
            putString("ACCESS_TOKEN", accessToken)
                .putString("REFRESH_TOKEN", refreshToken).apply()
        }
    }

    // Funções para pegar os tokens quando o Retrofit precisar
    fun getAccessToken(): String? = sharedPreferences.getString("ACCESS_TOKEN", null)

    fun getRefreshToken(): String? = sharedPreferences.getString("REFRESH_TOKEN", null)

    fun clearTokens() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }
}