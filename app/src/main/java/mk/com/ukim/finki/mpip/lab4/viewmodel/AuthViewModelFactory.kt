package mk.com.ukim.finki.mpip.lab4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mk.com.ukim.finki.mpip.lab4.repository.AuthRepository

class AuthViewModelFactory(
    private val authRepository: AuthRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(authRepository) as T
    }
}