package com.example.movieapplication.network.firebase

import com.example.movieapplication.network.model.UserModel
import com.example.movieapplication.user.User
import com.example.movieapplication.util.ConfigInfo
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("UNCHECKED_CAST")
object FirebaseInteractor {

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ConfigInfo.FIREBASE_URL)
        .client(OkHttpClient())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private var service: FirebaseApiService

    init {
        service = retrofit.create(FirebaseApiService::class.java)
    }

    private val auth = FirebaseAuth.getInstance()
    private val functions = FirebaseFunctions.getInstance()
    private var currentUser: FirebaseUser? = null
        set(value) {
            field = value
            if (value != null)
                subscribeToTopic()
        }
    private val storage = FirebaseStorage.getInstance()
    private val messaging = FirebaseMessaging.getInstance()

    private const val REGISTER = "register"
    private const val ADD_TO_FAVOURITES = "addFavourite"
    private const val REMOVE_FAVOURITE = "removeFavourite"
    private const val GET_FAVOURITES = "getFavourites"
    private const val SAVE_PROFILE_PICTURE_PATH = "addProfilePicturePath"
    private const val GIVE_RATING = "giveRating"
    private const val DELETE_RATING = "deleteRating"
    private const val SEND_REQUEST = "sendRequest"
    private const val ACCEPT_REQUEST = "acceptRequest"
    private const val DELETE_REQUEST = "removeRequest"
    private const val DELETE_FRIEND = "removeFriend"
    private const val RECOMMEND = "recommendMovie"
    private const val REMOVE_RECOMMENDATION = "removeRecommendation"


    init {
        currentUser = auth.currentUser
    }

    fun loggedIn(): Boolean {
        return currentUser != null
    }

    private fun subscribeToTopic() {
        messaging.subscribeToTopic(getUID()!!)
    }

    fun getUID(): String? {
        return currentUser?.uid
    }

    fun login(email: String, password: String, success: () -> Unit, error: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    currentUser = auth.currentUser
                    success()
                } else {
                    error(getException(task))
                }
            }
    }

    fun logOut() {
        messaging.unsubscribeFromTopic(getUID()!!)
        auth.signOut()
        currentUser = null
        User.username = null
        User.favouriteList?.clear()
        User.ratingList?.clear()
        User.friendList?.clear()
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

    fun uploadPicture(data: ByteArray, success: () -> Unit, error: (String) -> Unit) {
        val reference = storage.reference.child("images/${getUID()}.jpg")
        reference.putBytes(data).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                reference.downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful) {
                        User.picture_path = it.result?.toString()
                        saveProfilePicturePath(success, error)
                    } else {
                        error(getException(it))
                    }
                }
            } else {
                error(getException(task))
            }
        }

    }

    private fun saveProfilePicturePath(success: () -> Unit, error: (String) -> Unit) {
        val data = hashMapOf(
            "userid" to getUID(),
            "imgUrl" to User.picture_path
        )
        callFunction<HashMap<String, String>>(SAVE_PROFILE_PICTURE_PATH, data)
            .addOnCompleteListener { task ->
                if (!isTaskSuccessful(task)) {
                    error(getException(task))
                } else {
                    success()
                }
            }
    }

    fun changePassword(password: String, success: () -> Unit, error: (String) -> Unit) {
        currentUser?.updatePassword(password)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                success()
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

    suspend fun getUserInfo(): Response<UserModel> {
        return service.getUserInfo(getUID())
    }

    suspend fun getAllUser() = service.getAllUser(getUID())

    suspend fun getFriends() = service.getFriends(getUID())

    suspend fun getReceivedRequests() = service.getReceivedRequests(getUID())

    suspend fun getRecommendations() = service.getRecommendations(getUID())


    fun addToFavourites(id: String, type: String, success: () -> Unit, error: (String) -> Unit) {
        val data = hashMapOf(
            "uid" to getUID(),
            "id" to id,
            "type" to type
        )

        callFunction<HashMap<String, String>>(ADD_TO_FAVOURITES, data)
            .addOnCompleteListener { task ->
                if (isTaskSuccessful(task)) {
                    success()
                } else {
                    error(getException(task))
                }
            }
    }

    fun removeFromFavourites(id: String, type: String, success: () -> Unit, error: (String) -> Unit) {
        val data = hashMapOf(
            "uid" to getUID(),
            "id" to id,
            "type" to type
        )
        callFunction<HashMap<String, String>>(REMOVE_FAVOURITE, data)
            .addOnCompleteListener { task ->
                if (isTaskSuccessful(task)) {
                    success()
                } else {
                    error(getException(task))
                }
            }
    }

    fun giveRating(id: String, type: String, rating: Int, success: () -> Unit, error: (String) -> Unit) {
        val data = hashMapOf(
            "uid" to getUID(),
            "id" to id,
            "type" to type,
            "rating" to rating
        )
        callFunction<HashMap<String, String>>(GIVE_RATING, data)
            .addOnCompleteListener { task ->
                if (isTaskSuccessful(task)) {
                    success()
                } else {
                    error(getException(task))
                }
            }
    }

    fun deleteRating(id: String, type: String, success: () -> Unit, error: (String) -> Unit) {
        val data = hashMapOf(
            "uid" to getUID(),
            "id" to id,
            "type" to type
        )
        callFunction<HashMap<String, String>>(DELETE_RATING, data)
            .addOnCompleteListener { task ->
                if (isTaskSuccessful(task)) {
                    User.removeRating(id, type)
                    success()
                } else {
                    error(getException(task))
                }
            }
    }

    fun getFavouritesTo(id: String, success: (ArrayList<HashMap<String, String>>) -> Unit, error: (String) -> Unit) {
        val data = hashMapOf(
            "uid" to id
        )
        callFunction<HashMap<String, Any>>(GET_FAVOURITES, data)
            .addOnCompleteListener { task ->
                if (isTaskSuccessful(task)) {
                    val favourites = task.result!!["favourites"] as ArrayList<HashMap<String, String>>
                    success(favourites)
                } else {
                    error(getException(task))
                }
            }
    }

    fun sendRequest(user: String, success: () -> Unit, error: (String) -> Unit) {
        val data = hashMapOf(
            "fromid" to getUID(),
            "toid" to user
        )
        callFunction<HashMap<String, String>>(SEND_REQUEST, data)
            .addOnCompleteListener { task ->
                if (isTaskSuccessful(task)) {
                    success()
                } else {
                    error(getException(task))
                }
            }
    }

    fun acceptRequest(user: String, success: () -> Unit, error: (String) -> Unit) {
        val data = hashMapOf(
            "fromid" to user,
            "toid" to getUID()
        )
        callFunction<HashMap<String, String>>(ACCEPT_REQUEST, data)
            .addOnCompleteListener { task ->
                if (isTaskSuccessful(task)) {
                    success()
                } else {
                    error(getException(task))
                }
            }
    }

    fun deleteRequest(fromID: String?, toID: String?, success: () -> Unit, error: (String) -> Unit) {
        val data = hashMapOf(
            "fromid" to fromID,
            "toid" to toID
        )
        callFunction<HashMap<String, String>>(DELETE_REQUEST, data)
            .addOnCompleteListener { task ->
                if (isTaskSuccessful(task)) {
                    success()
                } else {
                    error(getException(task))
                }
            }
    }

    fun deleteFriend(user: String, success: () -> Unit, error: (String) -> Unit) {
        val data = hashMapOf(
            "firstID" to getUID(),
            "secondID" to user
        )

        callFunction<HashMap<String, String>>(DELETE_FRIEND, data)
            .addOnCompleteListener { task ->
                if (isTaskSuccessful(task)) {
                    success()
                } else {
                    error(getException(task))
                }
            }
    }

    fun recommend(friendID: String, id: String, type: String, success: () -> Unit, error: (String) -> Unit) {
        val data = hashMapOf(
            "fromid" to getUID(),
            "toid" to friendID,
            "id" to id,
            "type" to type
        )

        callFunction<HashMap<String, String>>(RECOMMEND, data)
            .addOnCompleteListener { task ->
                if (isTaskSuccessful(task)) {
                    success()
                } else {
                    error(getException(task))
                }
            }
    }

    fun deleteRecommendation(friendID: String, id: String, type: String, success: () -> Unit, error: (String) -> Unit) {
        val data = hashMapOf(
            "uid" to getUID(),
            "friendid" to friendID,
            "id" to id,
            "type" to type
        )
        callFunction<HashMap<String, String>>(REMOVE_RECOMMENDATION, data)
            .addOnCompleteListener { task ->
                if (isTaskSuccessful(task)) {
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

    private fun <T> isTaskSuccessful(task: Task<T>): Boolean {
        if (task.isSuccessful && task.result != null) {
            val data = task.result!! as HashMap<String, Any>
            if (data["result"]!! as String == "OK")
                return true
        }
        return false
    }

    private fun <T> getException(task: Task<T>): String {
        return if (task.exception != null && task.exception!!.message != null) {
            task.exception!!.message!!
        } else if (task.result != null) {
            val data = task.result as HashMap<String, Any>
            data["message"] as String
        } else {
            "Something wrong happened!"
        }
    }
}