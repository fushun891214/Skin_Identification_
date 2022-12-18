package com.example.Skin_Identification_

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class IdentifyingHistory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_identifying_history)

        val db = Firebase.firestore

        var users = Users()

        val results = arrayListOf<String>()

        val listView = findViewById<ListView>(R.id.listView)

        users.name = getUserProfileName(users.name)
        users.email = getUserProfileEmail(users.email)
        users.uid = getUserProfileUid(users.uid)

        //查詢資料庫內容
        db.collection("users")
            .whereEqualTo("uid",users.uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
//                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
//                    users.check_uid_correct = document.data["uid"].toString()
//                    users.check_docid_correct = document.data["docid"].toString()

//                    Log.d("123", document.data["identification_result"].toString())
                    results.add(document.data["identification_result"].toString())
//                    results.add("HELLO")

//                    findViewById<TextView>(R.id.textView_born_attribute).text = document.data["born"].toString()

//                    findViewById<ListView>(R.id.listView).adapter = ArrayAdapter(this,
//                        android.R.layout.simple_list_item_1, results
//                    )

//                    Log.d("540","HELLO")

                    listView.adapter = ArrayAdapter(this,
                        android.R.layout.simple_list_item_1, results
                    )
//                        Log.d("check",users.check_docid_correct)
//                        if (document.data["email"].toString() == users.email ){
//                            users.docid = document.data["docid"].toString()
//                            check = false
//                            login = true
//                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        findViewById<ImageButton>(R.id.apphome_imageButton4).setOnClickListener{
            val  intent = Intent(this,IdentifyingHistory::class.java)
            startActivity(intent)
        }


        findViewById<ImageButton>(R.id.apphome_imageButton5).setOnClickListener {
            val  intent = Intent(this,AppHome::class.java)
            intent.putExtra("choose_photo",true)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.apphome_imageButton2).setOnClickListener {
            val  intent = Intent(this,AppHome::class.java)
            intent.putExtra("photograph",true)
            startActivity(intent)
        }
    }
    fun apphome_sendMessage3(view: View) {
        val  intent = Intent(this,AppHome::class.java)
        startActivity(intent)
    }

    fun apphome_sendMessage4(view: View) {
        val  intent = Intent(this,AppHome::class.java)
        startActivity(intent)
    }

    fun  apphome_sendMessage6(view: View){
        val  intent = Intent(this,PersonnalImformation::class.java)
        startActivity(intent)
    }

    fun apphome_sendMessage7(view: View){
        val uri: Uri = Uri.parse("https://line.me/R/ti/p/~@847bpzej") // missing 'http://' will cause crashed
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

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
}