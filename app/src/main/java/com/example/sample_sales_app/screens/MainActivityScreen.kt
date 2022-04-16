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
import com.example.sample_sales_app.view_model.AppState
import com.example.sample_sales_app.view_model.MainActivityUserIntent
import com.example.sample_sales_app.view_model.MainViewModel

val modifier = Modifier
    .fillMaxSize()
    .background(color = Color.White)

@Composable
fun MainActivityScreen(
) {
    val state by MainViewModel.state.collectAsState()

    Box(
        modifier = modifier
    ) {
        when (state.innerState) {
            AppState.LOGIN -> LoginScreen(modifier)
            AppState.MAIN -> MainScreen(modifier)
        }
    }
}