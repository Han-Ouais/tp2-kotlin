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

    /**
     * Liste des utilisateurs chargés par la viewModel
     */
    private val _users = MutableLiveData<List<User>>()

    /**
     * Liste des utilisateurs chargés par le viewModel
     */
    val users: LiveData<List<User>> get() = _users

    /**
     * Charge tous les utilisateurs dans le viewModel
     */
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

    /**
     * Crée un nouvel utilisateur via le repository
     */
    fun createUser(newUser: User) {
        viewModelScope.launch {
            try {
                userRepository.createUser(newUser)?.let {
                    loadUsers() // Recharger les utilisateurs après la création
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erreur lors de la création de l'utilisateur", e)
            }
        }
    }

    /**
     * Met à jour un utilisateur dont l'identifiant est indiqué en paramètre, via le repository
     */
    fun updateUser(userId: Int, updatedUser: User) {
        viewModelScope.launch {
            try {
                userRepository.updateUser(userId, updatedUser)?.let {
                    loadUsers() // Recharger les utilisateurs après la mise à jour
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erreur lors de la mise à jour de l'utilisateur", e)
            }
        }
    }

    /**
     * Supprime un utilisateur en fonction de son identifiant en paramètre, via le repository
     */
    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            try {
                if (userRepository.deleteUser(userId)) {
                    loadUsers() // Recharger les utilisateurs après la suppression
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erreur lors de la suppression de l'utilisateur", e)
            }
        }
    }
}