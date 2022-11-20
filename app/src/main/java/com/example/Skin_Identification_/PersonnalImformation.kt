package com.example.Skin_Identification_

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class PersonnalImformation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.personnal_imformation)

        val head_stickers = findViewById<ImageView>(R.id.imformation_imageView02)
        val user_name = findViewById<TextView>(R.id.user_textView)

            val user = Firebase.auth.currentUser

        user?.let {

            val photoUrl = user.photoUrl
            val name = user.displayName

            user_name.text = name
            Picasso.with(this).load(photoUrl).resize(164,153).into(head_stickers)
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
            val  intent = Intent(this,PersonnalImformationEdit::class.java)
            startActivity(intent)
        }
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


//    private fun getUserProfile() {
//    // [START get_user_profile]
//    val user = Firebase.auth.currentUser
//        user?.let {
//            // Name, email address, and profile photo Url
//            val name = user.displayName
//            val email = user.email
//            val photoUrl = user.photoUrl
//
//            // Check if user's email is verified
//            val emailVerified = user.isEmailVerified
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            val uid = user.uid
//        }
//    // [END get_user_profile]
//    }
}