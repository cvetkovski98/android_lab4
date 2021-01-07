package mk.com.ukim.finki.mpip.lab4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mk.com.ukim.finki.mpip.lab4.model.Credentials
import mk.com.ukim.finki.mpip.lab4.model.Resource
import mk.com.ukim.finki.mpip.lab4.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _currentUser = MutableLiveData<Resource<FirebaseUser>>()
    val currentUser: LiveData<Resource<FirebaseUser>>
        get() = _currentUser


    fun signInUser(credentials: Credentials) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = authRepository.signIn(credentials)
            _currentUser.postValue(
                user
            )
        }
    }

    fun signOutUser() {
        authRepository.signOut()
        _currentUser.postValue(
            Resource.error(null, "User signed out")
        )
    }

    fun signUpUser(credentials: Credentials) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = authRepository.signUp(credentials)
            _currentUser.postValue(
                user
            )
        }
    }

    fun fetchCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _currentUser.postValue(
                authRepository.currentUser()
            )
        }
    }
}