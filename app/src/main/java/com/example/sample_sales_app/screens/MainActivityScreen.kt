package com.example.sample_sales_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.sample_sales_app.ui.theme.Purple200
import com.example.sample_sales_app.view_model.AppState
import com.example.sample_sales_app.view_model.MainActivityUserIntent
import com.example.sample_sales_app.view_model.MainViewModel

@Composable
fun MainActivityScreen(
) {
    val state by MainViewModel.state.collectAsState()

    val modifier = Modifier
        .fillMaxSize()
        .background(color = Purple200)
    Box(
        modifier = modifier
    ) {
        when (state.innerState) {
            AppState.LOGIN -> LoginScreen(modifier)
            AppState.MAIN -> MainScreen(modifier)
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier) {
    Box(modifier = modifier.clickable {
        MainViewModel.sendIntent(MainActivityUserIntent.Login)
    }) {
        Text(
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 30.sp,
            text = "Hello buddy, this is the Login Screen",
            modifier = modifier
                .wrapContentHeight()
        )
    }
}

@Composable
fun MainScreen(modifier: Modifier) {
    Text(
        textAlign = TextAlign.Center,
        color = Color.Cyan,
        fontSize = 30.sp,
        text = "You are now in the Main Screen",
        modifier = modifier.wrapContentHeight()
    )
}
