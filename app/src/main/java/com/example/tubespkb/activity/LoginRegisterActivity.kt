package com.example.tubespkb.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tubespkb.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TubesPKB)
        setContentView(R.layout.activity_login_register)
    }
}