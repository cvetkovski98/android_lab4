package mk.com.ukim.finki.mpip.lab4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mk.com.ukim.finki.mpip.lab4.repository.StudentRepository

class StudentViewModelFactory(
    private val studentRepository: StudentRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StudentViewModel(studentRepository) as T
    }
}