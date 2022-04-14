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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.sample_sales_app.ui.theme.SampleSalesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        appContext = applicationContext
        super.onCreate(savedInstanceState)
        setContent {
            SampleSalesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting()
                }
            }
        }
    }

    companion object {
        val onClick: () -> Unit = {
            clicks++
        }
        var clicks = 0
        private lateinit var appContext: Context
        fun getContext(): Context = appContext
    }
}

@Composable
fun Greeting() {
    var clicks by remember { mutableStateOf(MainActivity.clicks) }
    val loginViewModel = LoginViewModel.getInstance()
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable { clicks = addClick(clicks) },
        text = "You have clicked $clicks times",
        color = getColorFor(loginViewModel.state.value.loginStatus)
    )
}

fun getColorFor(loginStatus: LoginStatus): Color {
    return when (loginStatus) {
        LoginStatus.LOADED -> Color.Red
        else -> Color.Unspecified
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SampleSalesAppTheme {
        Greeting()
    }
}

fun addClick(value: Int): Int {
    return value + 1
}