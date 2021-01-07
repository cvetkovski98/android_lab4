package mk.com.ukim.finki.mpip.lab4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mk.com.ukim.finki.mpip.lab4.model.Resource
import mk.com.ukim.finki.mpip.lab4.model.Student
import mk.com.ukim.finki.mpip.lab4.repository.StudentRepository

class StudentViewModel(private val studentRepository: StudentRepository) : ViewModel() {
    private val _students = MutableLiveData<Resource<List<Student>>>()
    val students: LiveData<Resource<List<Student>>>
        get() = _students

    private val _studentDetails = MutableLiveData<Resource<Student>>()
    val studentDetails: LiveData<Resource<Student>>
        get() = _studentDetails

    private val _studentUpdateStatus = MutableLiveData<Resource<Unit>>()
    val studentUpdateStatus: LiveData<Resource<Unit>>
        get() = _studentUpdateStatus

    private val _studentCreateStatus = MutableLiveData<Resource<Unit>>()
    val studentCreateStatus: LiveData<Resource<Unit>>
        get() = _studentCreateStatus

    private val _studentRemoveStatus = MutableLiveData<Resource<Unit>>()
    val studentRemoveStatus: LiveData<Resource<Unit>>
        get() = _studentRemoveStatus

    fun createStudent(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            _studentCreateStatus.postValue(
                studentRepository.createStudent(student)
            )
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            _studentUpdateStatus.postValue(
                studentRepository.updateStudent(student)
            )
        }
    }

    fun removeStudent(studentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _studentRemoveStatus.postValue(
                studentRepository.removeStudent(studentId)
            )
        }
    }

    fun fetchStudentDetails(studentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _studentDetails.postValue(
                studentRepository.fetchStudentDetails(studentId)
            )
        }
    }

    @ExperimentalCoroutinesApi
    fun fetchStudentList() {
        viewModelScope.launch(Dispatchers.IO) {
            studentRepository.fetchStudentList().collect {
                _students.postValue(it)
            }
        }
    }

    companion object {
        private const val TAG = "StudentViewModel"
    }
}