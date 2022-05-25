package com.chesia.bangkitcapstoneproject.Local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.chesia.bangkitcapstoneproject.Networking.LoginResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>){

    //gangerti endpoint + butuh state
    fun getUser(): Flow<LoginResponseData> {
        return dataStore.data.map { preferences ->
            LoginResponseData(
                preferences[tokenKey] ?: "",
                preferences[endPoint] ?:""

            )
        }
    }

    suspend fun saveUser(loginResponseData: LoginResponseData){
        dataStore.edit{preferences ->
            preferences[tokenKey] = loginResponseData.token
            preferences[endPoint] = loginResponseData.endpoint
        }
    }

<<<<<<< Updated upstream
    companion object {
        @Volatile
        private var instance: UserPreferences? = null

        private val tokenKey = stringPreferencesKey("token")
        private val endPoint = stringPreferencesKey("state_login")
=======
    fun clearPreference(){
        editor.clear()
        editor.apply()
    }

    companion object{
        private const val PREFS_NAME = "user_pref"
        private const val TOKEN = "token"
    }
>>>>>>> Stashed changes

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return instance ?: synchronized(this) {
                val userPreference = UserPreferences(dataStore)
                instance = userPreference
                userPreference
            }
        }
    }
}