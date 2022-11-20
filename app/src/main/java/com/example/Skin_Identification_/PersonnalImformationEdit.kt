package com.example.Skin_Identification_

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class PersonnalImformationEdit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.personnal_imformation_edit)

        val db = Firebase.firestore

        val headStickers = findViewById<ImageView>(R.id.imformation_imageView02)
        val userName = findViewById<TextView>(R.id.user_textView)



        val user = Firebase.auth.currentUser
        var user_uid = ""
        var user_name = ""
        var user_mail = ""

        user?.let {
            val photoUrl = user.photoUrl
            user_uid = user.uid
            user_name = user.displayName.toString()
            user_mail = user.email.toString()
            Picasso.with(this).load(photoUrl).resize(164,153).into(headStickers)
            userName.text = user_name
        }



        findViewById<TextView>(R.id.imformation_textView03).setOnClickListener {
            signOut()
            val  intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.personnal_imageButton5).setOnClickListener {
            val  intent = Intent(this,AppHome::class.java)
            intent.putExtra("choose_photo",true)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.personnal_imageButton2).setOnClickListener {
            val  intent = Intent(this,AppHome::class.java)
            intent.putExtra("photograph",true)
            startActivity(intent)
        }

        findViewById<Switch>(R.id.switch_Below).setOnClickListener {
            val  intent = Intent(this,PersonnalImformation::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_save).setOnClickListener {

            //var users = Users()

//            var users_sex = findViewById<EditText>(R.id.textView_sex_attribute).text.toString()
//            var users_born = findViewById<EditText>(R.id.textView_born_attribute).text.toString()
//            var users_weight  = findViewById<EditText>(R.id.textView_weight_attribute).text.toString()
//            var users_height  = findViewById<EditText>(R.id.textView_height).text.toString()

            //Log.d("TAGString", users_sex)

//            users.sex = findViewById<EditText>(R.id.textView_sex_attribute).text.toString()
//            users.weight = findViewById<EditText>(R.id.textView_weight).text.toString()
//            users.height = findViewById<EditText>(R.id.textView_height).text.toString()
//            users.born = findViewById<EditText>(R.id.textView_born).text.toString()

            update_user()

//              var sex = "1"
//              var weight = "1"
//              var height = "1"
//              var born = "1"
//          Create a new user with a first, middle, and last name
//            val user = hashMapOf(
//                "uid" to users.uid,
//                "name" to users.name,
//                "born" to "1",
//                "sex" to users.sex,
//                "height" to users.height,
//                "weight" to users.weight,
//                "email" to users.email
//            )
//
//            // Add a new document with a generated ID
//            db.collection("users")
//                .add(user)
//                .addOnSuccessListener { documentReference ->
//                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//                }
//                .addOnFailureListener { e ->
//                    Log.w(ContentValues.TAG, "Error adding document", e)
//                }

            //listenToDocument()

//            db.collection("users")
//                .add(user)
//                .addOnSuccessListener { documentReference ->
//                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//                }
//                .addOnFailureListener { e ->
//                    Log.w(ContentValues.TAG, "Error adding document", e)
//                }

//            update_user(sex,weight,height,born)

            val  intent = Intent(this,PersonnalImformation::class.java)
            startActivity(intent)
        }
    }

    private var Firestore = FirebaseFirestore.getInstance()



    fun update_user (){

        var user = Users()
        user.born = findViewById<EditText>(R.id.textView_born_attribute).text.toString()
        user.sex = findViewById<EditText>(R.id.textView_sex_attribute).text.toString()
        user.weight = findViewById<EditText>(R.id.textView_height_attribute).text.toString()
        user.height = findViewById<EditText>(R.id.textView_weight_attribute).text.toString()

//        Log.d("TAGString", "南")

//        val user = hashMapOf(
//            "born" to findViewById<EditText>(R.id.textView_born_attribute).text.toString(),
//            "sex" to findViewById<EditText>(R.id.textView_sex_attribute).text.toString(),
//            "height" to findViewById<EditText>(R.id.textView_height_attribute).text.toString(),
//            "weight" to findViewById<EditText>(R.id.textView_weight_attribute).text.toString(),
//        )

//        Log.d("TAGString", findViewById<EditText>(R.id.textView_weight_attribute).text.toString())

        Firestore.collection("users")
            .document("aeSRUyIdlKzRoiZLjOOX")
            .set(user)

//        val db = Firebase.firestore
//        // Create a new user with a first, middle, and last name

//
//        db.collection("users").document("vk8qbUvzjSRfi5lquWX6")
//            .set(user)
//            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
//            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    fun personnal_sendMessage1(view: View) {
        val  intent = Intent(this,AppHome::class.java)
        startActivity(intent)
    }

    fun personnal_sendMessage3(view: View) {
        val  intent = Intent(this,AppHome::class.java)
        startActivity(intent)
    }

    fun  personnal_sendMessage6(view: View){
        val  intent = Intent(this,PersonnalImformation::class.java)
        startActivity(intent)
    }

    private fun signOut() {
        // [START auth_sign_out]
        Firebase.auth.signOut()
        // [END auth_sign_out]
    }
}