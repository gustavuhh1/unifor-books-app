package com.unifor.booksapp.data.session

import android.content.Context
import android.content.SharedPreferences

/**
 * Gerencia a sessão do usuário, armazenando tokens JWT e dados básicos
 * em SharedPreferences de forma segura (modo privado).
 */
class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "unifor_books_session"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_MATRICULA = "user_matricula"
        private const val KEY_USER_ROLE = "user_role"
        private const val KEY_USER_CRIADO_EM = "user_criado_em"

        @Volatile
        private var instance: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return instance ?: synchronized(this) {
                instance ?: SessionManager(context.applicationContext).also { instance = it }
            }
        }
    }

    fun saveSession(
        accessToken: String,
        refreshToken: String,
        userId: String,
        userName: String,
        userEmail: String,
        userMatricula: String,
        userRole: String,
        userCriadoEm: String = ""
    ) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .putString(KEY_USER_ID, userId)
            .putString(KEY_USER_NAME, userName)
            .putString(KEY_USER_EMAIL, userEmail)
            .putString(KEY_USER_MATRICULA, userMatricula)
            .putString(KEY_USER_ROLE, userRole)
            .putString(KEY_USER_CRIADO_EM, userCriadoEm)
            .apply()
    }

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)
    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)
    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)
    fun getUserName(): String? = prefs.getString(KEY_USER_NAME, null)
    fun getUserEmail(): String? = prefs.getString(KEY_USER_EMAIL, null)
    fun getUserMatricula(): String? = prefs.getString(KEY_USER_MATRICULA, null)
    fun getUserRole(): String? = prefs.getString(KEY_USER_ROLE, null)
    fun getUserCriadoEm(): String? = prefs.getString(KEY_USER_CRIADO_EM, null)

    fun isLoggedIn(): Boolean = getAccessToken() != null

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
