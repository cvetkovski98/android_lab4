package mk.com.ukim.finki.mpip.lab4.util

import mk.com.ukim.finki.mpip.lab4.factories.AuthViewModelFactory
import mk.com.ukim.finki.mpip.lab4.factories.StudentViewModelFactory
import mk.com.ukim.finki.mpip.lab4.repository.AuthRepository
import mk.com.ukim.finki.mpip.lab4.repository.StudentRepository

object FactoryInjector {

    fun getAuthViewModel(): AuthViewModelFactory {
        return AuthViewModelFactory(AuthRepository)
    }

    fun getStudentViewModel(): StudentViewModelFactory {
        return StudentViewModelFactory(StudentRepository)
    }
}