package com.arguvio.tp2Kotlin.viewModel

import android.util.Log
import androidx.compose.material3.Text
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arguvio.tp2Kotlin.models.User
import com.arguvio.tp2Kotlin.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    fun loadUsers() {
        viewModelScope.launch {
            try {
                Log.d("UserViewModel", "Avant de charger les utilisateurs")
                val userList = userRepository.getUsers()
                _users.value = userList
                Log.d("UserViewModel", "Utilisateurs chargés avec succès")
                for (user in _users.value!!) {
                    Log.d("UserViewModel", "User : ${user._id} | Name : ${user.name} | Email : ${user.email} | Password : ${user.password}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erreur lors du chargement des utilisateurs", e)
            }
        }
    }
}