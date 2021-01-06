package mk.com.ukim.finki.mpip.lab4.repository

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class AuthRepository(private val context: Context) {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signUp(email: String, password: String): Task<AuthResult> {
        return mAuth.createUserWithEmailAndPassword(email, password)
    }

    fun signIn(email: String, password: String): Task<AuthResult> {
        return mAuth.signInWithEmailAndPassword(email, password)
    }

    fun signOut() {
        return mAuth.signOut()
    }

    companion object {
        private var INSTANCE: AuthRepository? = null
        fun getInstance(context: Context): AuthRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthRepository(context).also { INSTANCE = it }
            }

        private const val TAG = "AuthRepository"
    }
}