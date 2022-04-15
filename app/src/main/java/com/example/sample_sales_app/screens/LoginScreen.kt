package com.example.sample_sales_app.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sample_sales_app.view_model.MainActivityUserIntent
import com.example.sample_sales_app.view_model.MainViewModel

enum class LoginScreenTags {
    GREETING, LOGIN_INSTRUCTION, LOGIN_SCREEN
}

enum class LoginScreenMessages(val message: String) {
    GREETING("Welcome to Sample Sales Application!"),
    INSTRUCTION("Click anywhere to start")
}

@Composable
fun LoginScreen(modifier: Modifier) {
    Box(modifier = modifier
        .testTag(LoginScreenTags.LOGIN_SCREEN.name)
        .clickable {
        MainViewModel.sendIntent(MainActivityUserIntent.Login)
    }) {
        Column(
            modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(72.dp))
            LoginScreenText(
                text = LoginScreenMessages.GREETING.message,
                modifier = Modifier
                    .testTag(LoginScreenTags.GREETING.name)
                    .weight(2f)
            )
            Emoji(
                text = "👋",
                modifier = Modifier
                    .weight(2f)
                    .padding(top = 24.dp)
            )
            Spacer(Modifier.height(32.dp))
            LoginScreenText(
                text = LoginScreenMessages.INSTRUCTION.message,
                fontWeight = FontWeight.Light,
                fontSize = 20.sp,
                modifier = Modifier
                    .testTag(LoginScreenTags.LOGIN_INSTRUCTION.name)
                    .weight(2f)
            )
            Spacer(Modifier.height(16.dp))
            ArrowsBlock(modifier = Modifier.weight(5f))
            Spacer(Modifier.height(72.dp))
        }
    }
}

@Composable
fun LoginScreenText(
    text: String,
    fontWeight: FontWeight = FontWeight.Bold,
    fontSize: TextUnit = 30.sp,
    modifier: Modifier
) {
    Text(
        textAlign = TextAlign.Center,
        color = Color.Black,
        fontSize = fontSize,
        text = text,
        fontFamily = FontFamily.Monospace,
        fontWeight = fontWeight,
        modifier = modifier
    )
}

@Composable
fun Emoji(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        fontSize = 40.sp,
        modifier = modifier
    )
}

@Composable
fun ArrowsBlock(modifier: Modifier) {
    Box(modifier.width(160.dp)) {
        ArrowsShape()
    }
}

@Composable
fun ArrowsShape() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val modifier = Modifier.weight(1f)

        @Composable
        fun ArrowEmoji(text: String) = Emoji(
            text = text,
            modifier = modifier
        )

        Row(horizontalArrangement = Arrangement.Center) {

            ArrowEmoji(text = "↖️")
            ArrowEmoji(text = "⬆️")
            ArrowEmoji(text = "↗️")
        }
        Row(horizontalArrangement = Arrangement.Center) {
            ArrowEmoji(text = "⬅️")
            Spacer(modifier = Modifier.width(48.dp))
            ArrowEmoji(text = "➡️")
        }
        Row(horizontalArrangement = Arrangement.Center) {
            ArrowEmoji(text = "↙️")
            ArrowEmoji(text = "⬇️")
            ArrowEmoji(text = "↘️")
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
    )
}