package mk.com.ukim.finki.mpip.lab4.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import mk.com.ukim.finki.mpip.lab4.model.Resource
import mk.com.ukim.finki.mpip.lab4.model.Student

@ExperimentalCoroutinesApi
object StudentRepository {
    private val studentDb: DatabaseReference = Firebase.database.reference.child("students")
    private const val TAG = "StudentRepository"

    fun createStudent(student: Student): Flow<Resource<Unit>> = callbackFlow {
        Log.i(TAG, "updateOrCreateStudent: Creating student with id: ${student.studentId}")
        studentDb.child(student.studentId).setValue(student).addOnCompleteListener { l ->
            if (l.isSuccessful) {
                offer(Resource.success(Unit))
            } else {
                l.exception?.let { exp ->
                    exp.message?.let { msg ->
                        offer(Resource.error(Unit, msg))
                    }
                }
            }
        }
        awaitClose()
    }

    fun fetchStudentList(): Flow<Resource<List<Student>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val studentMap = snapshot.getValue<Map<String, Student>>()
                if (studentMap != null) {
                    offer(Resource.success(studentMap.values.toList()))
                } else {
                    offer(Resource.success(listOf<Student>()))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                offer(Resource.error(listOf<Student>(), error.message))
            }
        }

        studentDb.addValueEventListener(listener)
        awaitClose()
    }

    fun fetchStudentDetails(studentId: String): Flow<Resource<Student>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val student = snapshot.getValue<Student>()
                if (student != null) {
                    offer(Resource.success(student))
                } else {
                    offer(Resource.success(Student()))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                offer(Resource.error(Student(), error.message))
            }
        }

        studentDb.child(studentId)
            .addListenerForSingleValueEvent(listener)

        awaitClose()
    }

    fun removeStudent(studentId: String): Flow<Resource<Unit>> = callbackFlow {
        Log.i(TAG, "removeStudent: Removing student with id: $studentId")
        studentDb.child(studentId).removeValue().addOnCompleteListener { l ->
            if (l.isSuccessful) {
                offer(Resource.success(Unit))
            } else {
                l.exception?.let { exp ->
                    exp.message?.let { msg ->
                        offer(Resource.error(Unit, msg))
                    }
                }
            }
        }
        awaitClose()
    }

    fun updateStudent(student: Student): Flow<Resource<Unit>> = callbackFlow {
        Log.i(TAG, "updateStudent: Updating student with id: ${student.studentId}")

        val studentValues = student.toMap()

        val update = hashMapOf<String, Any>(
            student.studentId to studentValues
        )

        studentDb.updateChildren(update).addOnCompleteListener { l ->
            if (l.isSuccessful) {
                offer(Resource.success(Unit))
            } else {
                l.exception?.let { exp ->
                    exp.message?.let { msg ->
                        offer(Resource.error(Unit, msg))
                    }
                }
            }
        }

        awaitClose()
    }
}