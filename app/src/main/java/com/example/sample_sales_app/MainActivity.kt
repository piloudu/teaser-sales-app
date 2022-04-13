package com.example.sample_sales_app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewModelScope
import com.example.sample_sales_app.ui.theme.SampleSalesAppTheme
import com.example.sample_sales_app.view_models.LoginViewModel.Companion.getCache
import com.example.sample_sales_app.view_models.LoginViewModel.Companion.getInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        appContext = applicationContext
        super.onCreate(savedInstanceState)
        setContent {
            SampleSalesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }

    companion object {
        private lateinit var appContext: Context
        fun getContext(): Context = appContext
    }
}

@Composable
fun Greeting(name: String) {
    val coroutineScope = rememberCoroutineScope()
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable {
                coroutineScope.launch {
                    getCache()
                }
            },
        text = "Hello $name!"
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SampleSalesAppTheme {
        Greeting("Android")
    }
}