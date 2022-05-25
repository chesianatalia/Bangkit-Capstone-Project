package com.chesia.bangkitcapstoneproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.chesia.bangkitcapstoneproject.Local.UserPreferences
import com.chesia.bangkitcapstoneproject.Networking.LoginResponseData
import kotlinx.coroutines.launch

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