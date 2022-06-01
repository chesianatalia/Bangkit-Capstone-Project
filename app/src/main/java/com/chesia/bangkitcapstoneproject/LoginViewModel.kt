package com.chesia.bangkitcapstoneproject

import androidx.lifecycle.ViewModel
import com.chesia.bangkitcapstoneproject.Local.LoginPreferences


class LoginViewModel(private val pref: LoginPreferences) : ViewModel(){
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