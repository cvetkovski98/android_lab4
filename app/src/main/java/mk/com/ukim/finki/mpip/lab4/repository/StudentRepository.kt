package mk.com.ukim.finki.mpip.lab4.repository

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
import kotlinx.coroutines.tasks.await
import mk.com.ukim.finki.mpip.lab4.model.Resource
import mk.com.ukim.finki.mpip.lab4.model.Student

object StudentRepository {
    private val studentDb: DatabaseReference = Firebase.database.reference.child("students")

    @ExperimentalCoroutinesApi
    suspend fun fetchStudentList(): Flow<Resource<List<Student>>> = callbackFlow {
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
        offer(Resource.loading(null))

        studentDb.addValueEventListener(listener)

        awaitClose()
    }

    suspend fun fetchStudentDetails(studentId: String): Resource<Student> {
        lateinit var resource: Resource<Student>
        studentDb.child(studentId).get()
            .addOnSuccessListener {
                val student = it.getValue<Student>()
                resource = if (student != null) {
                    Resource.success(student)
                } else {
                    Resource.error(Student(), "Student not found")
                }
            }.addOnFailureListener {
                it.message?.let { msg ->
                    resource = Resource.error(null, msg)
                } ?: run {
                    resource = Resource.error(null, "There was an error creating the student")
                }
            }.await()

        return resource
    }

    suspend fun createStudent(student: Student): Resource<Unit> {
        lateinit var resource: Resource<Unit>

        studentDb.child(student.studentId)
            .setValue(student)
            .addOnSuccessListener {
                resource = Resource.success(Unit)
            }
            .addOnFailureListener {
                it.message?.let { msg ->
                    resource = Resource.error(null, msg)
                } ?: run {
                    resource = Resource.error(null, "There was an error creating the student")
                }
            }.await()

        return resource
    }

    suspend fun removeStudent(studentId: String): Resource<Unit> {
        lateinit var resource: Resource<Unit>

        studentDb.child(studentId)
            .removeValue()
            .addOnSuccessListener {
                resource = Resource.success(Unit)
            }
            .addOnFailureListener {
                it.message?.let { msg ->
                    resource = Resource.error(null, msg)
                } ?: run {
                    resource = Resource.error(null, "There was an error removing the student")
                }
            }.await()

        return resource
    }

    suspend fun updateStudent(student: Student): Resource<Unit> {
        lateinit var resource: Resource<Unit>

        val studentValues = student.toMap()
        val update = hashMapOf<String, Any>(
            student.studentId to studentValues
        )

        studentDb.updateChildren(update)
            .addOnSuccessListener {
                resource = Resource.success(Unit)
            }
            .addOnFailureListener {
                it.message?.let { msg ->
                    resource = Resource.error(null, msg)
                } ?: run {
                    resource = Resource.error(null, "There was an error updating the student")
                }
            }.await()

        return resource
    }
}