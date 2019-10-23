package com.example.movieapplication.network.firebase

import com.example.movieapplication.user.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.functions.FirebaseFunctions

@Suppress("UNCHECKED_CAST")
object FirebaseInteractor {

    private val auth = FirebaseAuth.getInstance()
    private val functions = FirebaseFunctions.getInstance()
    private var currentUser: FirebaseUser? = null

    private const val REGISTER = "register"
    private const val GET_USER_DATA = "getUserData"

    init {
        currentUser = auth.currentUser
    }

    fun loggedIn(): Boolean {
        return currentUser != null
    }

    private fun getUID(): String? {
        return currentUser?.uid
    }

    fun autoLogin(success: () -> Unit, error: (String) -> Unit) {
        val data = hashMapOf("uid" to getUID())
        callFunction<HashMap<String, String>>(GET_USER_DATA, data)
            .addOnCompleteListener { task ->
                if (isTaskSuccessful(task)) {
                    val resultData = task.result!!
                    User.username = resultData["username"]
                    success()
                } else {
                    error(getException(task))
                }
            }
    }

    fun login(email: String, password: String, success: () -> Unit, error: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    currentUser = auth.currentUser
                    getUserData(success, error)
                } else {
                    error(getException(task))
                }
            }
    }

    fun logOut() {
        auth.signOut()
        User.username = null
    }

    fun createUser(email: String, password: String, username: String, success: () -> Unit, error: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    currentUser = auth.currentUser
                    saveUserNameToDatabase(username, success, error)
                } else {
                    error(getException(task))
                }
            }
    }

    private fun saveUserNameToDatabase(username: String, success: () -> Unit, error: (String) -> Unit) {
        val data = hashMapOf(
            "uid" to getUID(),
            "username" to username
        )

        callFunction<HashMap<String, String>>(REGISTER, data)
            .addOnCompleteListener { task ->
                if (isTaskSuccessful(task)) {
                    User.username = username
                    success()
                } else {
                    error(getException(task))
                }
            }
    }

    private fun getUserData(success: () -> Unit, error: (String) -> Unit) {
        val data = hashMapOf(
            "uid" to currentUser?.uid
        )
        callFunction<HashMap<String, String>>(GET_USER_DATA, data)
            .addOnCompleteListener { task ->
                if (isTaskSuccessful(task)) {
                    val resultData = task.result!!
                    User.username = resultData["username"]
                    success()
                } else {
                    error(getException(task))
                }
            }
    }

    private fun <T> callFunction(function: String, data: Any): Task<T> {
        return functions.getHttpsCallable(function)
            .call(data)
            .continueWith { task ->
                val result = task.result?.data as T
                result
            }
    }

    private fun isTaskSuccessful(task: Task<HashMap<String, String>>): Boolean {
        if (task.isSuccessful && task.result != null) {
            val data = task.result!!
            if (data["result"].equals("OK"))
                return true
        }
        return false
    }

    private fun <T> getException(task: Task<T>): String {
        return if (task.exception != null && task.exception!!.message != null) {
            task.exception!!.message!!
        } else if(task.result != null) {
            val data = task.result as HashMap<String, String>
            data["message"]!!
        } else {
            "Something wrong happened!"
        }
    }
}