package com.chesia.bangkitcapstoneproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

import com.chesia.bangkitcapstoneproject.Local.UserPreferences
import com.chesia.bangkitcapstoneproject.Networking.LoginResponseData


class LoginViewModel(private val pref: UserPreferences) : ViewModel(){
//    fun saveToken(user:LoginResponseData){
//        viewModelScope.launch{
//            pref.saveUser(LoginResponseData(user.token,user.endpoint))
//        }
//    }
//
//    fun getUser(): LiveData<LoginResponseData>{
//        return pref.getUser().asLiveData()
//    }
}