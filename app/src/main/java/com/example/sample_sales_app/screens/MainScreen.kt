package com.example.sample_sales_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.outlinedButtonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sample_sales_app.data_model.Currency
import com.example.sample_sales_app.data_model.Currency.*
import com.example.sample_sales_app.ui.theme.Purple700
import com.example.sample_sales_app.ui.theme.White
import com.example.sample_sales_app.view_model.MainActivityUserIntent
import com.example.sample_sales_app.view_model.MainViewModel

enum class MainScreenTags {
    DROPDOWN_INSTRUCTIONS, DROPDOWN, CURRENCY_INSTRUCTIONS, RESULT
}

enum class MainScreenMessages(val message: String) {
    HEADER("Sample Sales App"),
    DROPDOWN_INITIAL("Select an order"),
    RESULT("Total amount: ")
}

@Composable
fun isVertical(): Boolean {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    return screenHeight > screenWidth
}

@Composable
fun MainScreen(
    modifier: Modifier
) {

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopBar(modifier = Modifier.height(32.dp))
        Spacer(modifier = Modifier.weight(1f))
        DropdownPanel(
            modifier = Modifier
                .testTag(MainScreenTags.DROPDOWN.name)
                .weight(2f)
        )
        if (isVertical()) Spacer(modifier = Modifier.weight(1f))
        if (isVertical()) {
            Column(Modifier.weight(5f)) {
                CurrencyButtonRow(currencies = listOf(EUR, USD))
                Spacer(Modifier.weight(1f))
                CurrencyButtonRow(currencies = listOf(CAD, AUD))
            }
        } else Row(Modifier) {
            CurrencyButtonRow(
                modifier = Modifier.weight(3f), currencies = listOf(
                    EUR, USD, CAD, AUD
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier
                .weight(1f)
                .testTag(MainScreenTags.RESULT.name),
            text = MainScreenMessages.RESULT.message + "32,28",
            fontSize = 30.sp
        )
        if (isVertical()) Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun TopBar(modifier: Modifier) {
    Box(
        modifier = modifier
            .background(Purple700)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = MainScreenMessages.HEADER.message,
            fontSize = 20.sp,
            color = White
        )
    }
}

@Composable
fun DropdownPanel(
    modifier: Modifier
) {
    val state by MainViewModel.state.collectAsState()
    val orderCodes = state.cache.orders.map { it.sku }.distinct()
    var orderCode: String by remember { mutableStateOf(MainScreenMessages.DROPDOWN_INITIAL.message) }
    var expanded: Boolean by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current

    Box(modifier) {
        Row(
            Modifier
                .clickable {
                    expanded = !expanded
                },
            horizontalArrangement = Arrangement.Center
        ) {
            val fontSize = 30.sp
            if (orderCode == MainScreenMessages.DROPDOWN_INITIAL.message) {
                Text(
                    text = orderCode,
                    fontSize = fontSize,
                    color = Color.Gray,
                    modifier = Modifier.alpha(0.7f)
                )
            } else Text(
                orderCode,
                fontSize = fontSize
            )
            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width((configuration.screenWidthDp.dp / 2))
                    .height(300.dp)
            ) {
                orderCodes.forEach { order ->
                    val selected = state.mainScreenInfo.selectedOrder == order
                    val style = if (selected)
                        MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    else MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )

                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            orderCode = order
                            MainViewModel.sendIntent(MainActivityUserIntent.SelectOrder(order))
                        }) {
                        Text(
                            order,
                            style = style,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyButton(
    modifier: Modifier,
    currency: Currency,
) {
    val state by MainViewModel.state.collectAsState()
    var selected = state.mainScreenInfo.selectedCurrency == currency
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val backgroundColor = if (selected) Color.Blue else Color.LightGray

    val buttonSize = if (isVertical()) screenHeight / 6
    else screenWidth / 6

    OutlinedButton(
        modifier = Modifier
            .height(buttonSize)
            .width(buttonSize)
            .clickable {
            },
        shape = RoundedCornerShape(15),
        colors = outlinedButtonColors(
            backgroundColor = backgroundColor,
            contentColor = Color.White
        ),
        onClick = {
            selected = !selected
            MainViewModel.sendIntent(MainActivityUserIntent.SelectCurrency(currency))
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = currency.name,
                fontSize = 30.sp,
            )
        }
    }
}

@Composable
fun CurrencyButtonRow(
    modifier: Modifier = Modifier,
    currencies: List<Currency>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        for ((index, currency) in currencies.withIndex()) {
            if (index == 0) Spacer(Modifier.width(32.dp))
            CurrencyButton(
                modifier = Modifier
                    .testTag(currency.name)
                    .weight(6f),
                currency = currency
            )
            if (index != currencies.size - 1) Spacer(Modifier.weight(1f))
            if (index == currencies.size - 1) Spacer(Modifier.width(32.dp))
        }
    }
}

@Preview()
@Preview(showBackground = true, device = Devices.AUTOMOTIVE_1024p, widthDp = 1080, heightDp = 720)
@Composable
fun MainScreenPreview() {
    MainScreen(modifier = Modifier.fillMaxSize())
}