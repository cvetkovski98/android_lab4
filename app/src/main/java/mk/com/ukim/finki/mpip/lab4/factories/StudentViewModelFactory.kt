package mk.com.ukim.finki.mpip.lab4.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mk.com.ukim.finki.mpip.lab4.repository.StudentRepository
import mk.com.ukim.finki.mpip.lab4.viewmodel.StudentViewModel

class StudentViewModelFactory(
    private val studentRepository: StudentRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StudentViewModel(studentRepository) as T
    }
}