package com.example.sample_sales_app.view_model.ui

import org.junit.Rule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.sample_sales_app.screens.LoginScreen
import com.example.sample_sales_app.screens.modifier
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class LoginScreenTest {

   @get:Rule
   val composeTestRule = createComposeRule()

   @DisplayName("Should have welcome text")
   @Test
   fun `login screen has the welcome text`() {
       composeTestRule.setContent {
          LoginScreen(modifier)
       }
       val text = composeTestRule.onNodeWithTag("Welcome text").fetchSemanticsNode().config
       text shouldBe "Welcome to Sample Sales Application!"
   }
}