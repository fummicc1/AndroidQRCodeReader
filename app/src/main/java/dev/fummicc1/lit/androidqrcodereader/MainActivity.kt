package dev.fummicc1.lit.androidqrcodereader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.zxing.integration.android.IntentIntegrator
import dev.fummicc1.lit.androidqrcodereader.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    companion object {
        val baseURL: String = "https://www.googleapis.com/books/v1/"
    }

    lateinit var binding: ActivityMainBinding

    lateinit var bookService: BookService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            Log.d("MainActivity#onCreate", "butoon is tapped.")
            IntentIntegrator(this).initiateScan()
        }

        val gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        bookService = retrofit.create(BookService::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        IntentIntegrator.parseActivityResult(requestCode, resultCode, data)?.let { result ->
            val contents = "isbn:${result.contents}"

            runBlocking(Dispatchers.IO) {
                kotlin.runCatching {
                    bookService.getBook(contents)
                }
            }.onSuccess {
                binding.textView.text = it.items.first().volumeInfo.title
            }.onFailure {
                Log.d("onActivityResult", it.toString())
            }
        }
    }
}