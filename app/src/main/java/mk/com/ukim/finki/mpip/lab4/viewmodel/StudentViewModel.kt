package mk.com.ukim.finki.mpip.lab4.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import mk.com.ukim.finki.mpip.lab4.model.Resource
import mk.com.ukim.finki.mpip.lab4.model.Student
import mk.com.ukim.finki.mpip.lab4.repository.StudentRepository

class StudentViewModel(private val studentRepository: StudentRepository) : ViewModel() {
    private var _students = MutableLiveData<Resource<List<Student>>>()
    private var _studentDetails = MutableLiveData<Resource<Student>>()

    init {
        initListener()
    }

    fun getStudents(): LiveData<Resource<List<Student>>> {
        return _students
    }

    fun getStudentDetails(): LiveData<Resource<Student>> {
        return _studentDetails
    }

    fun createStudent(student: Student) {
        studentRepository.createStudent(student).addOnCompleteListener { l ->
            if (l.isSuccessful) {
                Log.i(TAG, "createStudent: success")
            } else {
                Log.e(TAG, "createStudent: error ${l.exception?.message}")
            }
        }
    }

    fun updateStudent(student: Student) {
        studentRepository.updateStudent(student).addOnCompleteListener { l ->
            if (l.isSuccessful) {
                Log.i(TAG, "updateStudent: success")
            } else {
                l.exception?.let { ex ->
                    ex.message?.let { mes ->
                        Log.e(TAG, "updateStudent: $mes")
                    }
                }
            }
        }
    }

    fun removeStudent(studentId: String) {
        studentRepository.removeStudent(studentId).addOnCompleteListener { l ->
            if (l.isSuccessful) {
                Log.i(TAG, "removeStudent: success")
            } else {
                l.exception?.let { ex ->
                    ex.message?.let { mes ->
                        Log.e(TAG, "removeStudent: $mes")
                    }
                }
            }
        }
    }

    fun fetchStudentDetails(studentId: String) {
        val studentDetailsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val student = snapshot.getValue<Student>()
                if (student != null) {
                    _studentDetails.value = Resource.success(student)
                } else {
                    _studentDetails.value = Resource.success(Student())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _studentDetails.value = Resource.error(Student(), error.message)
            }
        }

        studentRepository.addStudentDetailsListener(studentId, studentDetailsListener)
    }

    private fun initListener() {
        val studentListListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val indexStudentMap = snapshot.getValue<Map<String, Student>>()
                if (indexStudentMap != null) {
                    _students.value = Resource.success(indexStudentMap.values.toList())
                } else {
                    _students.value = Resource.success(listOf())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _students.value = Resource.error(listOf(), error.message)
            }
        }
        studentRepository.addStudentListListener(studentListListener)
    }

    companion object {
        private const val TAG = "StudentViewModel"
    }
}