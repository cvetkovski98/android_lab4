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

@ExperimentalCoroutinesApi
class StudentViewModel(private val studentRepository: StudentRepository) : ViewModel() {
    private val _students = MutableLiveData<Resource<List<Student>>>()
    private val _studentDetails = MutableLiveData<Resource<Student>>()
    private val _studentUpdateStatus = MutableLiveData<Resource<Unit>>()
    private val _studentCreateStatus = MutableLiveData<Resource<Unit>>()
    private val _studentRemoveStatus = MutableLiveData<Resource<Unit>>()

    fun getStudents(): LiveData<Resource<List<Student>>> {
        return _students
    }

    fun getStudentDetails(): LiveData<Resource<Student>> {
        return _studentDetails
    }

    fun getUpdateStatus(): LiveData<Resource<Unit>> {
        return _studentUpdateStatus
    }

    fun getCreateStatus(): LiveData<Resource<Unit>> {
        return _studentCreateStatus
    }

    fun getRemoveStatus(): LiveData<Resource<Unit>> {
        return _studentRemoveStatus
    }

    fun createStudent(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            studentRepository.createStudent(student).collect {
                _studentCreateStatus.postValue(it)
            }
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            studentRepository.updateStudent(student).collect {
                _studentUpdateStatus.postValue(it)
            }
        }
    }

    fun removeStudent(studentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            studentRepository.removeStudent(studentId).collect {
                _studentRemoveStatus.postValue(it)
            }
        }
    }

    fun fetchStudentDetails(studentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            studentRepository.fetchStudentDetails(studentId).collect {
                _studentDetails.postValue(it)
            }
        }
    }

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