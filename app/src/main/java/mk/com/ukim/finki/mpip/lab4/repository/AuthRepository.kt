package mk.com.ukim.finki.mpip.lab4.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import mk.com.ukim.finki.mpip.lab4.model.Credentials
import mk.com.ukim.finki.mpip.lab4.model.Resource

object AuthRepository {

    private val mAuth: FirebaseAuth = Firebase.auth

    fun currentUser(): Resource<FirebaseUser> {
        lateinit var resource: Resource<FirebaseUser>
        val user: FirebaseUser? = mAuth.currentUser
        resource = if (user != null) {
            Resource.success(user)
        } else {
            Resource.error(null, "No user signed in")
        }

        return resource
    }


    suspend fun signUp(credentials: Credentials): Resource<FirebaseUser> {
        lateinit var resource: Resource<FirebaseUser>

        val email = credentials.email
        val password = credentials.password

        if (email.isBlank() || password.isBlank()) {
            return Resource.error(null, "Email or password is blank")
        }

        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    resource = currentUser()
                }
                .addOnFailureListener {
                    it.message?.let { msg ->
                        resource = Resource.error(null, msg)
                    } ?: run {
                        resource = Resource.error(null, "There was an error creating the user")
                    }
                }.await()
        } catch (e: Exception) {
            var message = e.message
            if (message == null) {
                message = "Error occurred"
            }
            resource = Resource.error(null, message)
        }

        return resource
    }

    suspend fun signIn(credentials: Credentials): Resource<FirebaseUser> {
        lateinit var resource: Resource<FirebaseUser>

        val email = credentials.email
        val password = credentials.password

        if (email.isBlank() || password.isBlank()) {
            return Resource.error(null, "Email or password is blank")
        }

        try {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    resource = currentUser()
                }
                .addOnFailureListener {
                    it.message?.let { msg ->
                        resource = Resource.error(null, msg)
                    } ?: run {
                        resource = Resource.error(null, "There was an error signing in")
                    }
                }.await()
        } catch (e: Exception) {
            var message = e.message
            if (message == null) {
                message = "Error occurred"
            }
            resource = Resource.error(null, message)
        }
        return resource
    }

    fun signOut() {
        return mAuth.signOut()
    }


}