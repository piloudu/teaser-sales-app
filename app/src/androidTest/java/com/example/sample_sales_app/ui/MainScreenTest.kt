package com.example.sample_sales_app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.sample_sales_app.screens.MainScreen
import com.example.sample_sales_app.screens.MainScreenTags
import com.example.sample_sales_app.ui.theme.SampleSalesAppTheme
import com.example.sample_sales_app.view_model.MainViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.DisplayName

@DisplayName("Main Screen")
class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun withMainScreenContentSetter(test: ComposeContentTestRule.() -> Unit) {
        composeTestRule.setContent {
            val state by MainViewModel.state.collectAsState()
            SampleSalesAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(Modifier.fillMaxSize(), state)
                }
            }
        }
        composeTestRule.test()
    }

    @DisplayName("has dropdown instructions message")
    @Test
    fun main_screen_displays_dropdown_instructions_message() {
        withMainScreenContentSetter {
            onNodeWithTag(MainScreenTags.DROPDOWN_INSTRUCTIONS .name, true).assertIsDisplayed()
        }
    }

    @DisplayName("has dropdown panel")
    @Test
    fun main_screen_displays_dropdown_panel() {
        withMainScreenContentSetter {
            onNodeWithTag(MainScreenTags.DROPDOWN .name, true).assertIsDisplayed()
        }
    }

    @DisplayName("has currency instructions message")
    @Test
    fun main_screen_displays_currency_instructions_message() {
        withMainScreenContentSetter {
            onNodeWithTag(MainScreenTags.CURRENCY_INSTRUCTIONS .name, true).assertIsDisplayed()
        }
    }


    @DisplayName("has EUR button")
    @Test
    fun main_screen_displays_EUR_button() {
        withMainScreenContentSetter {
            onNodeWithTag(MainScreenTags.EUR .name, true).assertIsDisplayed()
        }
    }

    @DisplayName("has USD button")
    @Test
    fun main_screen_displays_USD_button() {
        withMainScreenContentSetter {
            onNodeWithTag(MainScreenTags.USD .name, true).assertIsDisplayed()
        }
    }

    @DisplayName("has CAD button")
    @Test
    fun main_screen_displays_CAD_button() {
        withMainScreenContentSetter {
            onNodeWithTag(MainScreenTags.CAD .name, true).assertIsDisplayed()
        }
    }

    @DisplayName("has AUD button")
    @Test
    fun main_screen_displays_AUD_button() {
        withMainScreenContentSetter {
            onNodeWithTag(MainScreenTags.AUD .name, true).assertIsDisplayed()
        }
    }

    @DisplayName("has result element")
    @Test
    fun main_screen_displays_result_element() {
        withMainScreenContentSetter {
            onNodeWithTag(MainScreenTags.RESULT .name, true).assertIsDisplayed()
        }
    }
}