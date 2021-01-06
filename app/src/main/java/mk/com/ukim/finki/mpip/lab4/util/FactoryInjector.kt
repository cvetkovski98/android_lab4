package mk.com.ukim.finki.mpip.lab4.util

import android.content.Context
import mk.com.ukim.finki.mpip.lab4.repository.AuthRepository
import mk.com.ukim.finki.mpip.lab4.repository.StudentRepository
import mk.com.ukim.finki.mpip.lab4.viewmodel.AuthViewModelFactory
import mk.com.ukim.finki.mpip.lab4.viewmodel.StudentViewModelFactory

object FactoryInjector {
    private fun getAuthRepository(ctx: Context): AuthRepository {
        return AuthRepository.getInstance(ctx)
    }

    fun getAuthViewModel(ctx: Context): AuthViewModelFactory {
        return AuthViewModelFactory(getAuthRepository(ctx))
    }

    fun getStudentViewModel(): StudentViewModelFactory {
        return StudentViewModelFactory(StudentRepository)
    }
}