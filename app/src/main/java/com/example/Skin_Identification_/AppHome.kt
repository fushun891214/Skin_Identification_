package com.example.Skin_Identification_

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.Skin_Identification_.ml.ModelUnquantMetadata
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.tensorflow.lite.support.image.TensorImage
import java.io.IOException
import kotlin.math.roundToInt


class AppHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.apphome_page)


        //預設選擇照片和拍照功能為false
        var choose_photo = intent.getBooleanExtra("choose_photo",false)
        var photograph = intent.getBooleanExtra("photograph",false)

        //如果為true就可以選擇照片
        if (choose_photo == true){
            photochoose_photograph()
        }
        //如果為true就可以拍照
        if (photograph == true){
            photograph()
        }

        //辨識功能--

        //APP拍照建立物件，放入機器人的框框裡面_6/8
        findViewById<ImageButton>(R.id.apphome_imageButton2).setOnClickListener {
            photograph()
        }

        //APP選擇照片建立成物件，放入機器人的框框裡面_6/8
        findViewById<ImageButton>(R.id.apphome_imageButton5).setOnClickListener {
            photochoose_photograph()
        }

        //--辨識功能

        findViewById<ImageButton>(R.id.apphome_imageButton4).setOnClickListener{
            val  intent = Intent(this,IdentifyingHistory::class.java)
            startActivity(intent)
        }

    }

    private var Firestore = FirebaseFirestore.getInstance()

    //照相
    fun photograph(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //用 try-catch 避免例外產生，若產生則顯示 Toast
        try {
            startActivityForResult(intent, 0) //發送 Intent

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "error", Toast.LENGTH_SHORT
            ).show()
        }
    }
    //選擇圖片
    fun photochoose_photograph(){
            val intent =
                Intent(Intent.ACTION_GET_CONTENT).setType("image/*")
            //用 try-catch 避免例外產生，若產生則顯示 Toast
            try {
                startActivityForResult(intent, 1) //發送 Intent
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this,
                    "error", Toast.LENGTH_SHORT
                ).show()
            }
        }

    fun apphome_sendMessage1(view: View) {
        val  intent = Intent(this,AppHome::class.java)
        startActivity(intent)
    }

    fun apphome_sendMessage6(view: View) {
        val  intent = Intent(this,PersonnalImformation::class.java)
        startActivity(intent)
    }

    fun apphome_sendMessage7(view: View){
        val uri: Uri = Uri.parse("https://line.me/R/ti/p/~@847bpzej") // missing 'http://' will cause crashed
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    //辨識功能--

    // 接收結果
    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //識別返回對象及執行結果
        if (requestCode == 0 && resultCode == RESULT_OK) {
            val image = data?.extras?.get("data") ?: return //取得資料
            val bitmap = image as Bitmap //將資料轉換成 Bitmap
            val imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageBitmap(bitmap) //使用 Bitmap 設定圖像
            //???_6/8
            imageView.rotation = 90f //使 ImageView 旋轉順時針90度
            recognizeImage(bitmap) //使用 Bitmap 進行辨識
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val uri = data!!.data
            val imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageURI(uri)
            imageView.rotation = 0f
            val drawable = imageView.drawable as BitmapDrawable //從imageView取得資料，轉換成Bitmap
            val bitmap = drawable.bitmap
            recognizeImage(bitmap) //使用 Bitmap 進行辨識
        }
    }

    // 辨識圖像
    private fun recognizeImage(bitmap: Bitmap) {
        try {
            // Loads my custom model
            val model = ModelUnquantMetadata.newInstance(this)

            // Creates inputs for reference.
            val tensorImage = TensorImage.fromBitmap(bitmap)

            // Runs model inference and gets result.
            val outputs = model.process(tensorImage)
                .probabilityAsCategoryList.apply {
                    sortByDescending { it.score } // 排序，由高到低
                }

            //取得辨識結果與可信度
            val result = arrayListOf<String>()
            for (output in outputs) {
                val label = output.label
                val score: Int = (output.score * 100).roundToInt()
                result.add("皮膚是 $label 的可能性為 $score %")
            }

            updataresult(result.toString())

            val listView = findViewById<ListView>(R.id.listView)
            listView.adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, result
            )

        } catch (e: IOException) {
            e.printStackTrace()
        }
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

//    fun getData(sex:String,weight:String,height:String,born:String){
//
//        var users = Users()
//
//        users.name = getUserProfileName(users.name)
//        users.email = getUserProfileEmail(users.email)
//        users.uid = getUserProfileUid(users.uid)
//
//        val db = Firebase.firestore
//
//        db.collection("users")
//            .whereEqualTo("uid",users.uid)
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
////                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
////                          personnel_information = document.data.toString()
////                    findViewById<TextView>(R.id.textView_sex_attribute).text = document.data["sex"].toString()
////                    Log.d("test123",document.data["sex"].toString())
////                    findViewById<TextView>(R.id.textView_weight_attribute).text = document.data["weight"].toString()
////                    findViewById<TextView>(R.id.textView_height_attribute).text = document.data["height"].toString()
////                    findViewById<TextView>(R.id.textView_born_attribute).text = document.data["born"].toString()
//
//                    users.sex = document.data["sex"].toString()
//                    users.weight = document.data["weight"].toString()
//                    users.height = document.data["height"].toString()
//                    users.born = document.data["born"].toString()
//
////                   Log.d("514",users.sex)
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w(ContentValues.TAG, "Error getting documents.", exception)
//            }
//
//        return getData(users.sex,users.weight,users.height,users.born)
//
//    }

    //--辨識功能

    fun updataresult(result:String){

        var users = Users()

        users.name = getUserProfileName(users.name)
        users.email = getUserProfileEmail(users.email)
        users.uid = getUserProfileUid(users.uid)

        val db = Firebase.firestore

        db.collection("users")
            .whereEqualTo("uid",users.uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    users.sex = document.data["sex"].toString()
                    users.weight = document.data["weight"].toString()
                    users.height = document.data["height"].toString()
                    users.born = document.data["born"].toString()
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        users.Identification_result = result


//        for (i in result){
//            users.Identification_result = arrayListOf(i.toString()).toString()
//        }


        Firestore.collection("users")
            .document(users.uid)
            .set(users)
    }
}
