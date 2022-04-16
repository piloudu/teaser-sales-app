package com.example.sample_sales_app.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.sample_sales_app.data_model.CacheData
import com.example.sample_sales_app.data_model.CurrencyChange
import com.example.sample_sales_app.data_model.Order
import com.example.sample_sales_app.view_model.AppState
import com.example.sample_sales_app.view_model.MainActivityState
import com.example.sample_sales_app.view_model.MainViewModel

enum class MainScreenTags {
    DROPDOWN_INSTRUCTIONS, DROPDOWN, CURRENCY_INSTRUCTIONS, EUR, AUD, USD, CAD, RESULT
}

enum class MainScreenMessages {
    DROP_DOWN_INSTRUCTIONS,
}

private val previewState = MainActivityState(
    innerState = AppState.MAIN,
    isLoading = false,
    cache = CacheData(
        currencyChanges = listOf(
        ),
        orders = listOf(
            Order(
                sku = "T2006",
                amount = "10.00",
                currency = "EUR"
            )
        )
    )
)

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(modifier = Modifier.fillMaxSize(), state = previewState)
}

@Composable
fun MainScreen(
    modifier: Modifier,
    state: MainActivityState
) {
    val orderCodes: List<String> = state.cache.orders.map { it.sku }
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DropDownPanel(orderCodes = orderCodes,
            modifier = Modifier.testTag(MainScreenTags.DROPDOWN.name))

    }
}

@Composable
fun DropDownPanel(
    orderCodes: List<String>,
    modifier: Modifier
) {
    var orderCode: String by remember { mutableStateOf("Select an order") }
    var expanded: Boolean by remember { mutableStateOf(false) }

    Row(modifier
        .fillMaxWidth()
        .clickable {
        expanded = !expanded
    },
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = orderCode)
        Icon(imageVector = Icons.Filled.ArrowDropDown, null)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.verticalScroll(ScrollState(0))
        ) {
            orderCodes.forEach { selectedOrderCode ->
                val isSelected = orderCode == selectedOrderCode
                val style = if (isSelected)
                    MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.secondary
                    )
                else MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colors.onSurface
                )

                DropdownMenuItem(onClick = {
                    expanded = false
                    orderCode = selectedOrderCode
                }) {
                    Text(orderCode, style = style)
                }
            }
        }
    }
}