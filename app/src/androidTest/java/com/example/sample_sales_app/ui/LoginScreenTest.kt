package com.example.sample_sales_app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.sample_sales_app.screens.LoginScreenMessages
import com.example.sample_sales_app.screens.LoginScreen
import com.example.sample_sales_app.ui.theme.SampleSalesAppTheme
import org.junit.jupiter.api.DisplayName
import org.junit.Test

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
       composeTestRule.onNodeWithText(LoginScreenMessages.GREETING.message).assertIsDisplayed()
   }
}