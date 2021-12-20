package com.example.crudapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.crudapp.ui.theme.Emerald
import com.example.crudapp.ui.theme.GrayBlue

@Composable
fun SaveButton(enabled: Boolean?, onClick: () -> Unit) {

    Button(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Emerald,
            contentColor = GrayBlue),
        enabled = enabled?: true
    ) {
        Text("SAVE")
    }
}