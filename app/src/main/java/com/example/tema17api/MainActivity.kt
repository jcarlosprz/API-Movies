package com.example.tema17api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tema17api.genres.GenerosFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.container, GenerosFragment()).commit()
    }
}