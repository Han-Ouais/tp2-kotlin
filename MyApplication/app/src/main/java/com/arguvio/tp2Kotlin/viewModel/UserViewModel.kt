package com.arguvio.tp2Kotlin.viewModel

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
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    fun loadUsers() {
        viewModelScope.launch {
            try {
                val userList = userRepository.getUsers()
                _users.value = userList
            } catch (e: Exception) {
                // Gérer l'erreur, par exemple, afficher un message à l'utilisateur
            }
        }
    }
}