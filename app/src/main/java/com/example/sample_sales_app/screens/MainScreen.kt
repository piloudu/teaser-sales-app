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
    DROPDOWN_INITIAL("Select an order")
}

@Composable
fun MainScreen(
    modifier: Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopBar(modifier = Modifier.height(32.dp))
        Spacer(Modifier.weight(1f))
        DropdownPanel(
            modifier = Modifier
                .testTag(MainScreenTags.DROPDOWN.name)
                .weight(2f)
        )
        Spacer(Modifier.weight(1f))
        if (screenHeight > screenWidth) {
            Column(Modifier.weight(3f)) {
                CurrencyButtonRow(modifier = Modifier, currencies = listOf(EUR, USD))
                Spacer(Modifier.weight(1f))
                CurrencyButtonRow(modifier = Modifier, currencies = listOf(CAD, AUD))
                Spacer(Modifier.weight(2f))
            }
        } else Row(Modifier) {
            CurrencyButtonRow(modifier = Modifier.weight(3f), currencies = listOf(EUR, USD))
            CurrencyButtonRow(modifier = Modifier.weight(3f), currencies = listOf(CAD, AUD))
        }
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

                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            orderCode = order
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
    //resImage: Int,
) {
    val state by MainViewModel.state.collectAsState()
    var selected = state.mainScreenInfo.selectedCurrency == currency
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val backgroundColor = if (selected) Color.Blue else Color.Gray

    val buttonSize = if (screenHeight > screenWidth) screenHeight / 6
    else screenWidth / 6

    OutlinedButton(
        modifier = Modifier
            .height(buttonSize)
            .width(buttonSize)
            .clickable {
            },
        shape = RoundedCornerShape(15),
        colors = outlinedButtonColors(
            backgroundColor = Color.LightGray,
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
            //Image(painter = painterResource(id = ), contentDescription = null)
            Text(
                text = currency.name,
                fontSize = 30.sp,
            )
        }
    }
}

@Composable
fun CurrencyButtonRow(
    modifier: Modifier,
    currencies: List<Currency>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.width(32.dp))
        CurrencyButton(
            modifier = Modifier
                .testTag(EUR.name)
                .weight(6f),
            currency = EUR
        )
        Spacer(modifier = Modifier.weight(1f))
        CurrencyButton(
            modifier = Modifier
                .testTag(USD.name)
                .weight(6f),
            currency = USD
        )
        Spacer(Modifier.width(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(modifier = Modifier.fillMaxSize())
}