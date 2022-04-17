package com.example.sample_sales_app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.sample_sales_app.data_model.Currency
import com.example.sample_sales_app.screens.MainScreen
import com.example.sample_sales_app.screens.MainScreenTags
import com.example.sample_sales_app.ui.theme.SampleSalesAppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.DisplayName

@DisplayName("Main Screen")
class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun withMainScreenContentSetter(test: ComposeContentTestRule.() -> Unit) {
        composeTestRule.setContent {
            SampleSalesAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(Modifier.fillMaxSize())
                }
            }
        }
        composeTestRule.test()
    }

    @DisplayName("has dropdown panel")
    @Test
    fun main_screen_displays_dropdown_panel() {
        withMainScreenContentSetter {
            onNodeWithTag(MainScreenTags.DROPDOWN_PANEL.name, true).assertIsDisplayed()
        }
    }

    @DisplayName("has EUR button")
    @Test
    fun main_screen_displays_EUR_button() {
        withMainScreenContentSetter {
            onNodeWithTag(Currency.EUR.name, true).assertIsDisplayed()
        }
    }

    @DisplayName("has USD button")
    @Test
    fun main_screen_displays_USD_button() {
        withMainScreenContentSetter {
            onNodeWithTag(Currency.USD.name, true).assertIsDisplayed()
        }
    }

    @DisplayName("has CAD button")
    @Test
    fun main_screen_displays_CAD_button() {
        withMainScreenContentSetter {
            onNodeWithTag(Currency.CAD.name, true).assertIsDisplayed()
        }
    }

    @DisplayName("has AUD button")
    @Test
    fun main_screen_displays_AUD_button() {
        withMainScreenContentSetter {
            onNodeWithTag(Currency.AUD.name, true).assertIsDisplayed()
        }
    }

    @DisplayName("has result element")
    @Test
    fun main_screen_displays_result_element() {
        withMainScreenContentSetter {
            onNodeWithTag(MainScreenTags.RESULT.name, true).assertIsDisplayed()
        }
    }

    @DisplayName("DropdownMenu displays when on DropdownPanel click")
    @Test
    fun dropdown_menu_displays_on_dropdown_panel_click() {
        withMainScreenContentSetter {
            val dropdownPanel = onNodeWithTag(MainScreenTags.DROPDOWN_PANEL.name, true)
            dropdownPanel.performClick()
            onNodeWithTag(MainScreenTags.DROPDOWN_MENU.name, true).assertIsDisplayed()
        }
    }
}