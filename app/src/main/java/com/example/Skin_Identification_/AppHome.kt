package com.example.Skin_Identification_

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import org.tensorflow.lite.support.image.TensorImage
import java.io.IOException
import android.widget.*

import com.example.Skin_Identification_.ml.ModelUnquantMetadata
import kotlin.math.roundToInt

class AppHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.apphome_page)

        var choose_photo = intent.getBooleanExtra("choose_photo",false)
        var photograph = intent.getBooleanExtra("photograph",false)

        if (choose_photo == true){
            photochoose_photograph()
        }

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

    }

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

            //將結果顯示於 ListView
            val listView = findViewById<ListView>(R.id.listView)
            listView.adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                result
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //--辨識功能
}
