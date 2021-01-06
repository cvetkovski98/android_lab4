package mk.com.ukim.finki.mpip.lab4.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mk.com.ukim.finki.mpip.lab4.model.Student

object StudentRepository {
    private val studentDb: DatabaseReference = Firebase.database.reference.child("students")
    private const val TAG = "StudentRepository"

    fun createStudent(student: Student): Task<Void> {
        Log.i(TAG, "updateOrCreateStudent: Creating student with id: ${student.studentId}")
        return studentDb.child(student.studentId).setValue(student)
    }

    fun addStudentListListener(listener: ValueEventListener) {
        Log.i(TAG, "addListener: Adding Database Listener")
        studentDb.addValueEventListener(listener)
    }

    fun addStudentDetailsListener(studentEditId: String, listener: ValueEventListener) {
        Log.i(TAG, "addListener: Adding Database Listener")
        studentDb.child(studentEditId).addListenerForSingleValueEvent(listener)
    }

    fun removeStudent(studentId: String): Task<Void> {
        Log.i(TAG, "removeStudent: Removing student with id: $studentId")
        return studentDb.child(studentId).removeValue()
    }

    fun updateStudent(student: Student): Task<Void> {
        Log.i(TAG, "updateStudent: Updating student with id: ${student.studentId}")

        val studentValues = student.toMap()

        val update = hashMapOf<String, Any>(
            student.studentId to studentValues
        )

        return studentDb.updateChildren(update)
    }
}