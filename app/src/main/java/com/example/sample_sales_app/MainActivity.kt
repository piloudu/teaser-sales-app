package com.example.sample_sales_app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.example.sample_sales_app.screens.MainActivityScreen
import com.example.sample_sales_app.ui.theme.SampleSalesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        appContext = applicationContext
        super.onCreate(savedInstanceState)
        setContent {
            SampleSalesAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainActivityScreen()
                }
            }
        }
    }

    companion object {
        private lateinit var appContext: Context
        fun getContext(): Context = appContext
    }
}