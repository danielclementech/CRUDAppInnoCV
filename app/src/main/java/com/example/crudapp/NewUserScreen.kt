package com.example.crudapp

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crudapp.MainActivity.Companion.padding16
import com.example.crudapp.components.SaveButton
import com.example.crudapp.model.UserModel
import com.example.crudapp.ui.theme.*
import com.example.crudapp.viewModel.UserViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.InternalCoroutinesApi
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@InternalCoroutinesApi
@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewUserScreen(navHost : NavController, viewModel: UserViewModel) {

    val postFailed = remember { mutableStateOf(false) }
    val nameEmpty = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf( "") }

    Column {
        TopAppBar(
            backgroundColor = Emerald.copy(0.5f),
            contentColor = DarkBlue,
        ) {
            Row {
                IconButton(onClick = {
                    navHost.navigate("home")
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic__11686_back_arrow_icon),
                        contentDescription = "back"
                    )
                }
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .background(LightBlue.copy(0.5f))
                .padding(padding16.dp)
        ){
            Text(
                text = stringResource(R.string.new_user_name_title),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            Spacer(Modifier.height(4.dp))
            TextField(value = name.value,
                onValueChange = {
                name.value = it
            })

            var datePicked : LocalDateTime? by remember {
                mutableStateOf(LocalDateTime.now())
            }

            val updatedDate = { date : Long? ->
                datePicked = LocalDateTime.ofInstant(
                    date?.let { Instant.ofEpochMilli(it) },
                    TimeZone.getDefault().toZoneId())
            }

            Text(
                text = stringResource(R.string.new_user_birthdate_title),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(Modifier.height(4.dp))

            DatePickerView(formatDate(datePicked.toString()), updatedDate)

            Spacer(Modifier.height(16.dp))

            SaveButton(enabled = true) {
                if (name.value.isNotEmpty()) {
                    viewModel.postUser(UserModel(0, name.value, datePicked))
                    viewModel.loadData()
                    navHost.navigate("home")
                }
                else {
                    nameEmpty.value = true
                }
            }

            if (postFailed.value) {

                //AlertDialog to alert that the user has not been created due to an unexpected error
                AlertDialog(
                    onDismissRequest = {
                    },
                    title = {
                        Text(stringResource(R.string.post_failed_title))
                    },
                    text = {
                        Text(stringResource(R.string.post_failed_text))
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                postFailed.value = false
                            }) {
                            Text(stringResource(R.string.ok_alert))
                        }
                    }
                )
            }

            if (nameEmpty.value) {

                //AlertDialog when the user wants to create a user but the name is empty
                AlertDialog(
                    onDismissRequest = {
                    },
                    title = {
                        Text(text = stringResource(R.string.name_empty_title))
                    },
                    text = {
                        Text(stringResource(R.string.name_empty_text))
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                nameEmpty.value = false
                            }) {
                            Text(stringResource(R.string.ok_alert))
                        }
                    }
                )
            }
        }
    }
}


