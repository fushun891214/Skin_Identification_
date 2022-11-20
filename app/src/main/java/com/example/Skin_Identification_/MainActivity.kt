package com.example.Skin_Identification_

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    var user = Users()

    //Google登入--

    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private lateinit var googleSignInClient: GoogleSignInClient

    //--Google登入

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.signup_page)

        //Google登入--

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // [END config_signin]


        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        //--Google登入

        findViewById<com.google.android.gms.common.SignInButton>(R.id.sign_in_button).setOnClickListener {

            signIn()

            user.name = getUserProfileName(user.name)
            user.email = getUserProfileEmail(user.email)
            user.uid = getUserProfileUid(user.uid)
            user.docid = getDocId(user.docid) //再來跨頁傳值

            checkCurrentUser()

            upload_user()
        }
    }

    private var Firestore = FirebaseFirestore.getInstance()

    private fun checkCurrentUser() {
        // [START check_current_user]
        val user = Firebase.auth.currentUser
        //如果user已經登入，就進入apphome
        if (user != null) {
            updateUI()
        //否則就重新登入一次
        } else {
            signIn()
        }
    }

    //Google登入--

    //取得user的名字
    private fun getUserProfileName(user_name:String): String {
        // [START get_user_profile]
        val user = Firebase.auth.currentUser
        var name = user_name

        user?.let {
            name = user.displayName.toString()
        }
        return name
        // [END get_user_profile]
    }

    //取得user的email
    private fun getUserProfileEmail(user_email:String):String {
        // [START get_user_profile]
        val user = Firebase.auth.currentUser
        var email = user_email

        user?.let {
            email = user.email.toString()
        }
        // [END get_user_profile]
        return email
    }

    //取得user的Uid
    private fun getUserProfileUid(user_uid:String): String {
        // [START get_user_profile]
        val user = Firebase.auth.currentUser
        var uid = user_uid

        user?.let {
            uid = user.uid.toString()
        }
        return uid
        // [END get_user_profile]
    }

    // [START on_start_check_user]
    //啟動app時，檢查user是否已經登入，如果已經登入了就直接跳轉到apphome
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI()
        }
    }
    // [END on_start_check_user]

//     [START onactivityresult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
//     [END onactivityresult]

//     [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }
    // [END auth_with_google]

    //取得生成檔案時的隨機ID
    fun getDocId(id:String) : String{
        var id  = Firestore.collection("users").document().id
        return id
    }

    //上傳user的資料
    fun upload_user (){
        Firestore.collection("users")
            //若沒有指定 文件的名稱會由系統產生
            .document(user.docid)
            .set(user)
    }



    // [START signin]
    //google登入
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]

    private fun updateUI(user: FirebaseUser?) { //10/3 如果已登入過就直接跳轉頁面
    }

    //更新介面
    private fun updateUI() { //10/3 如果已登入過就直接跳轉頁面
        val intent = Intent(this, AppHome::class.java)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
    //--Google登入
}

data class Users(
    var uid:String = "",
    var name:String = "",
    var email:String = "",
    var sex :String =  "",
    var weight:String = "",
    var height:String = "",
    var born:String = "",
    var docid:String = ""
)


//-------------------------註解and備援程式碼-----------------------//

//class MainActivity : AppCompatActivity() {
//
//    var user = Users()
//
//    val db = Firebase.firestore
//
//    //Google登入--
//
//    private lateinit var auth: FirebaseAuth
//    // [END declare_auth]
//
//    private lateinit var googleSignInClient: GoogleSignInClient
//
//    //--Google登入
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        supportActionBar?.hide()
//
//        setContentView(R.layout.signup_page)
//
//        //Google登入--
//
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//        // [END config_signin]
//
//
//        // [START initialize_auth]
//        // Initialize Firebase Auth
//        auth = Firebase.auth
//        // [END initialize_auth]
//
//        //--Google登入
//
////        onStart()
//
//        findViewById<com.google.android.gms.common.SignInButton>(R.id.sign_in_button).setOnClickListener {
//
//
//
//            signIn()
//
//            user.name = getUserProfileName(user.name)
//            user.email = getUserProfileEmail(user.email)
//            user.uid = getUserProfileUid(user.uid)
//            user.docid = getDocId(user.docid) //再來跨頁傳值
//
//
////            update_user(user_name, user_email,user_uid)
//
//            checkCurrentUser()
//
////            if (checkUserUid(user_uid)){ //檢查是否已上傳過
////                update_user(user_name, user_email,user_uid)
////            }
//
//            upload_user()
//
//
//
////            checkCurrentUser()
//
////            checkUserUid(user_uid)
//
//
//        }
//    }

//private var Firestore = FirebaseFirestore.getInstance()
//
//private fun checkCurrentUser() {
//    // [START check_current_user]
//    val user = Firebase.auth.currentUser
//
//    //如果user已經登入，就進入apphome
//    if (user != null) {
//        updateUI()
////            val intent = Intent(this, AppHome::class.java)
////            startActivity(intent)
//        //否則就重新登入一次
//    } else {
//        signIn()
////            val signInIntent = googleSignInClient.signInIntent
////            startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//    // [END check_current_user]
//}

//    private fun getDocument(useruid: String): Boolean {
//        // [START get_document]
//        var isEmpty = false
//        val docRef = db.collection("users")
//            .document("uid")
//        docRef.get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
//                    if(useruid == document.data?.get("uid").toString()){
//                        isEmpty = true
//                    }
//                } else {
//                    Log.d(TAG, "No such document")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "get failed with ", exception)
//            }
//        // [END get_document]
//        return isEmpty
//    }

//private fun updateUI(user: FirebaseUser?) { //10/3 如果已登入過就直接跳轉頁面
//        if (user != null){
//            val intent = Intent(this, AppHome::class.java)
//            startActivity(intent)On
//        }
//}

//上傳user的資料
//fun upload_user (){

// Create a new user with a first, middle, and last name
//        val user = hashMapOf(
//                "uid" to useruid,
//                "name" to username,
//                "born" to "",
//                "sex" to "",
//                "height" to "",
//                "weight" to "",
//                "email" to usermail
//            )

//        val id  = Firestore.collection("users").document().id

//    Firestore.collection("users")
//        //若沒有指定 文件的名稱會由系統產生
//        .document(user.docid)
//        .set(user)


//        Log.d("TAG123", userdocid)

    // Add a new document with a generated ID
//        db.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w(ContentValues.TAG, "Error adding document", e)
//            }

//        val data = HashMap<String, Any>()
//        val newUsersRef = db.collection("users").document()
//        Log.d("TAG123", newUsersRef.toString())
//        newUsersRef.set(data)
//}

//fun checkUserUid(user_uid: String): Boolean{
//    var checkuser_uid = ""
//    var isempty = false
//    db.collection("users")
//        .orderBy("uid") //10/3新增
//        .limit(25) //10/3新增
//        .whereEqualTo("uid",checkuser_uid)
//        .get()
//        .addOnSuccessListener { result ->
//            for (document in result) {
//                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
//                checkuser_uid = document.data["uid"].toString()
//            }
//        }
//        .addOnFailureListener { exception ->
//            Log.w(ContentValues.TAG, "Error getting documents.", exception)
//        }
//
//    if (user_uid == checkuser_uid){
//        isempty = true
//    }
//    return  isempty
//}

//private fun checkCurrentUser() {
//    // [START check_current_user]
//    val user = Firebase.auth.currentUser
//
//    //如果user已經登入，就進入apphome
//    if (user != null) {
//        updateUI()
////            val intent = Intent(this, AppHome::class.java)
////            startActivity(intent)
//        //否則就重新登入一次
//    } else {
//        signIn()
////            val signInIntent = googleSignInClient.signInIntent
////            startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//    // [END check_current_user]
//}

//Google登入--

//取得user的名字
//private fun getUserProfileName(user_name:String): String {
//    // [START get_user_profile]
//    val user = Firebase.auth.currentUser
//    var name = user_name
//
//    user?.let {
        // Name, email address, and profile photo Url
//        name = user.displayName.toString()
//            email = user.email.toString()
//            val photoUrl = user.photoUrl
//
//            // Check if user's email is verified
//            val emailVerified = user.isEmailVerified
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            val uid = user.uid

//    }
//    return name
    // [END get_user_profile]
//}

//取得user的email
//private fun getUserProfileEmail(user_email:String):String {
//    // [START get_user_profile]
//    val user = Firebase.auth.currentUser
//    var email = user_email
//
//    user?.let {
        // Name, email address, and profile photo Url
//            name = user.displayName.toString()
//        email = user.email.toString()
//            val photoUrl = user.photoUrl
//
//            // Check if user's email is verified
//            val emailVerified = user.isEmailVerified
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            val uid = user.uid

//    }
    // [END get_user_profile]
//    return email
//}

//取得user的Uid
//private fun getUserProfileUid(user_uid:String): String {
//    // [START get_user_profile]
//    val user = Firebase.auth.currentUser
//    var uid = user_uid
//
//    user?.let {
        // Name, email address, and profile photo Url
//            name = user.displayName.toString()
//            email = user.email.toString()
//            val photoUrl = user.photoUrl
//
//            // Check if user's email is verified
//            val emailVerified = user.isEmailVerified
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//        uid = user.uid.toString()
//
//    }
//    return uid
    // [END get_user_profile]
//}
