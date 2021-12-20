package com.example.crudapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crudapp.MainActivity.Companion.padding8
import com.example.crudapp.data.Mapper
import com.example.crudapp.model.UserModel
import com.example.crudapp.ui.theme.DarkBlue
import com.example.crudapp.ui.theme.Emerald
import com.example.crudapp.ui.theme.GrayBlue
import com.example.crudapp.ui.theme.LightBlue
import com.example.crudapp.viewModel.UserViewModel
import com.google.gson.Gson
import kotlinx.coroutines.InternalCoroutinesApi
import kotlin.math.absoluteValue

@InternalCoroutinesApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navHost : NavController, users: State<List<UserModel>?>, viewModel: UserViewModel) {

    val mapper = Mapper()
    var selectedIndex by remember { mutableStateOf(0) }

    Column {

        //TopAppBar with the icon to add a user
        TopAppBar(
            backgroundColor = Emerald.copy(0.5f),
            contentColor = DarkBlue,
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement= Arrangement.End) {
                IconButton(onClick = {
                    navHost.navigate("new_user")
                }) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(R.drawable.add_new_icon),
                        contentDescription = "add"
                    )
                }
            }
        }

        //Dropdown menu to order the elements

        var expanded by remember { mutableStateOf(false) }
        val items = listOf("Birthdate DESC", "Birthdate ASC", "Creation DESC", "Creation ASC", "Name DESC", "Name ASC")

        Box {
            Text(items[selectedIndex],
                modifier = Modifier
                .fillMaxWidth().clickable(onClick = { expanded = true })
                    .background(DarkBlue).padding(8.dp),
            color = GrayBlue)
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBlue)
                    .padding(padding8.dp)
            ) {
                items.forEachIndexed { index, s ->
                    DropdownMenuItem(onClick = {
                        selectedIndex = index
                        expanded = false
                    }) {
                        Text(text = s, color = GrayBlue)
                    }
                }
            }
        }

        //List of the users displayed
        LazyColumn(
            Modifier
                .background(LightBlue.copy(0.5f))
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Row {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.info_icon),
                        contentDescription = "info"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.info_list),
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            val mutableUsers = users.value?.toMutableList()
            orderBy(mutableUsers, selectedIndex.absoluteValue)

            mutableUsers?.let { it ->
                items(mutableUsers) { user ->
                    Spacer(Modifier.height(8.dp))
                    Row(modifier = Modifier.clickable {
                        val userJson = Gson().toJson(mapper.mapUserToResponse(user))
                        navHost.navigate("user/$userJson")
                    }) {
                        UserCard(user)
                    }
                }
            }
        }
    }
}

/**
 * Returns the list of the user ordered by [criteria].
 */
@RequiresApi(Build.VERSION_CODES.O)
fun orderBy(users: MutableList<UserModel>?, criteria: Int): MutableList<UserModel>? {
    when(criteria) {
        0 -> {
            users?.sortWith (compareBy ({ it.birthdate?.year }, {it.birthdate?.monthValue}, {it.birthdate?.dayOfMonth}))
            users?.reverse()
        }
        1 -> {
            users?.sortWith (compareBy ({ it.birthdate?.year }, {it.birthdate?.monthValue}, {it.birthdate?.dayOfMonth}))
        }
        2-> {
            users?.sortByDescending { it.id }
        }
        3 -> {
            users?.sortByDescending { it.id }
            users?.reverse()
        }
        4 -> {
            users?.sortByDescending { it.name?.lowercase() }
        }
        5 -> {
            users?.sortByDescending { it.name?.lowercase() }
            users?.reverse()
        }
    }
    return users
}