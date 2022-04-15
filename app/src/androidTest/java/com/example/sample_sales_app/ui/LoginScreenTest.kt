package com.example.sample_sales_app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import org.junit.Rule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.sample_sales_app.screens.LoginScreen
import com.example.sample_sales_app.ui.theme.SampleSalesAppTheme
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class LoginScreenTest {

   @get:Rule
   val composeTestRule = createComposeRule()

   @DisplayName("Should have welcome text")
   @Test
   fun login_screen_has_the_welcome_text() {
       composeTestRule.setContent {
           SampleSalesAppTheme {
               LoginScreen(Modifier.fillMaxSize())
           }
       }
       val text = composeTestRule.onNodeWithTag("Welcome text").fetchSemanticsNode().config
       text shouldBe "Welcome to Sample Sales Application!"
   }
}