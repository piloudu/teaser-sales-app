package com.example.sample_sales_app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.sample_sales_app.screens.LoginScreen
import com.example.sample_sales_app.screens.LoginScreenTags
import com.example.sample_sales_app.ui.theme.SampleSalesAppTheme
import org.junit.Rule
import org.junit.jupiter.api.DisplayName
import org.junit.Test

@DisplayName("LoginScreen")
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun withLoginScreenContentSetter(
        test: ComposeContentTestRule.() -> Unit
    ) {
        composeTestRule.setContent {
            SampleSalesAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    LoginScreen(modifier = Modifier.fillMaxSize())
                }
            }
        }
        composeTestRule.test()
    }

    @DisplayName("is the container box displayed")
    @Test
    fun login_screen_container_box_is_displayed() {
        withLoginScreenContentSetter {
            onNodeWithTag(LoginScreenTags.LOGIN_SCREEN.name).assertIsDisplayed()
        }
    }

    @DisplayName("has welcome message")
    @Test
    fun login_screen_displays_the_welcome_message() {
        withLoginScreenContentSetter {
            onNodeWithTag(LoginScreenTags.GREETING_MESSAGE.name, true).assertIsDisplayed()
        }
    }

    @DisplayName("has greeting emoji")
    @Test
    fun login_screen_displays_greeting_emoji() {
        withLoginScreenContentSetter {
            onNodeWithTag(LoginScreenTags.GREETING_EMOJI.name, true).assertIsDisplayed()
        }
    }

    @DisplayName("has instructions message")
    @Test
    fun login_screen_displays_instructions_message() {
        withLoginScreenContentSetter {
            onNodeWithTag(LoginScreenTags.LOGIN_INSTRUCTION.name, true).assertIsDisplayed()
        }
    }

    @DisplayName("has arrows block")
    @Test
    fun login_screen_displays_arrows_block() {
        withLoginScreenContentSetter {
            onNodeWithTag(LoginScreenTags.ARROWS.name, true).assertIsDisplayed()
        }
    }
}