package ru.vsu.cs.tp.richfamily.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.room.user.UserDB
import ru.vsu.cs.tp.richfamily.room.AppDataBase
import ru.vsu.cs.tp.richfamily.room.user.UserRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    val token = MutableLiveData<String>()
    val user =  MutableLiveData<UserDB>()
    private val repository: UserRepository

    init {
        val dao = AppDataBase.getDatabase(application).getUserDao()
        repository = UserRepository(dao)
        user.value = repository.currentUser
    }

    fun saveUser(userDB: UserDB) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(userDB)
    }
}