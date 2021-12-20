package com.example.crudapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crudapp.MainActivity.Companion.padding16
import com.example.crudapp.MainActivity.Companion.padding8
import com.example.crudapp.components.SaveButton
import com.example.crudapp.data.Mapper
import com.example.crudapp.data.UserResponseModel
import com.example.crudapp.model.UserModel
import com.example.crudapp.ui.theme.*
import com.example.crudapp.viewModel.UserViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@InternalCoroutinesApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserDetail(navHost : NavController, viewModel: UserViewModel, user: UserResponseModel) {

    val mapper = Mapper()
    val deleteClicked = remember { mutableStateOf(false) }
    val userToDisplay = mapper.mapUser(user)

    Column {

        //TopAppBar with the icon to go back
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
                .padding(padding16.dp)) {

            //User information to display
            UserInformation(user = userToDisplay, viewModel, navHost)

            Spacer(Modifier.height(padding16.dp))

            //Button to delete a user
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    deleteClicked.value = true
                },
                // Uses ButtonDefaults.ContentPadding by default
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 12.dp,
                    end = 20.dp,
                    bottom = 12.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red.copy(0.5f),
                    contentColor = GrayBlue
                )
            ) {
                Text(stringResource(R.string.delete_button))
            }
        }
        if(deleteClicked.value) {

            //AlertDialog to ask before deleting a user
            AlertDialog(
                onDismissRequest = {
                },
                title = {
                    Text(text = stringResource(R.string.delete_user_alert_title))
                },
                text = {
                    Text(stringResource(R.string.delete_user_alert_text))
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteUser(userToDisplay.id)
                            navHost.navigate("home")
                        }) {
                        Text(stringResource(R.string.delete_button))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            deleteClicked.value = false
                        }) {
                        Text(stringResource(R.string.cancel_alert))
                    }
                }
            )
        }
    }
}

@InternalCoroutinesApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserInformation(user: UserModel, viewModel: UserViewModel, navHost : NavController) {

    var newName by remember  { mutableStateOf(user.name) }

    user.name?.let {
        TextField(
        value = newName!!,
        onValueChange = { newName = it },
        label = { Text(text = "Name") },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Emerald.copy(0.5f),
            disabledIndicatorColor = DarkBlue,
            unfocusedIndicatorColor = DarkBlue,
            backgroundColor = GrayBlue,
        ))
    }

    var datePicked : LocalDateTime? by remember {
        mutableStateOf(user.birthdate)
    }

    user.birthdate?.let {

        val updatedDate = { date : Long? ->
            datePicked = LocalDateTime.ofInstant(
                date?.let { Instant.ofEpochMilli(it) },
                TimeZone.getDefault().toZoneId())
        }

        DatePickerView(formatDate(datePicked.toString()), updatedDate)
    }

    Spacer(Modifier.height(padding16.dp))

    SaveButton(enabled = dataChanged(newName, datePicked, user)) {
        val updatedUser = UserModel(user.id, newName, datePicked)
        viewModel.updateUser(updatedUser)
        navHost.navigate("home")
    }

    Spacer(Modifier.height(padding8.dp))

    if(!dataChanged(newName, datePicked, user)) {
        Row {
            Icon(
                painter = painterResource(R.drawable.alert_icon),
                contentDescription = "alert"
            )
            Spacer(Modifier.width(8.dp))
            Text(
                modifier = Modifier.align(Alignment.Bottom),
                text = stringResource(R.string.button_disabled),
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic
            )
        }
    }
}


/**
 * Returns the formatted day of the [date] deleting the time
 */
fun formatDate(date: String) : String {
    val splitted = date.split("T")
    return splitted[0]
}

/**
 * Returns if the name or the birthdate have been changed.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun dataChanged(newName: String?, datePicked: LocalDateTime?, user: UserModel) : Boolean {
    return newName != user.name || datePicked != user.birthdate
}