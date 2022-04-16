package com.example.sample_sales_app.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sample_sales_app.view_model.MainViewModel

enum class MainScreenTags {
    DROPDOWN_INSTRUCTIONS, DROPDOWN, CURRENCY_INSTRUCTIONS, EUR, AUD, USD, CAD, RESULT
}

enum class MainScreenMessages {
    DROP_DOWN_INSTRUCTIONS,
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(modifier = Modifier.fillMaxSize())
}

@Composable
fun MainScreen(
    modifier: Modifier
) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DropdownPanel(
            modifier = Modifier.testTag(MainScreenTags.DROPDOWN.name)
        )
    }
}

@Composable
fun DropdownPanel(
    modifier: Modifier
) {
    val state by MainViewModel.state.collectAsState()
    val orderCodes = state.cache.orders.map { it.sku }.distinct()
    var orderCode: String by remember { mutableStateOf("Select an order") }
    var expanded: Boolean by remember { mutableStateOf(false) }

    Row(
        modifier
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
            onDismissRequest = { expanded = false }
        ) {
            orderCodes.forEach { order ->
                val isSelected = orderCode == order
                val style = if (isSelected)
                    MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                else MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )

                DropdownMenuItem(onClick = {
                    expanded = false
                    orderCode = order
                }) {
                    Text(order, style = style)
                }
            }
        }
    }
}