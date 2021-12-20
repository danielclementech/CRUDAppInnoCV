package com.example.crudapp


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crudapp.model.UserModel
import com.example.crudapp.ui.theme.DarkBlue
import com.example.crudapp.ui.theme.GrayBlue
import com.example.crudapp.ui.theme.LightBlue
import java.time.LocalDateTime
import androidx.compose.ui.res.painterResource


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserCard(user: UserModel) {
    Card(
        backgroundColor = GrayBlue,
        contentColor = DarkBlue,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, DarkBlue)
    ) {
        Row (
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column (Modifier
                .padding(16.dp)) {
                user.name?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    user.birthdate?.let {
                        Row {
                            Icon(
                                painter = painterResource(R.drawable.birthday_icon),
                                contentDescription = "birthday"
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                modifier = Modifier.align(Alignment.Bottom),
                                text = displayBirthday(user.birthdate!!),
                                fontWeight = FontWeight.Medium,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun displayBirthday(birthday: LocalDateTime) : String {
    var date = ""
    date += birthday.dayOfMonth
    date += "/"
    date += birthday.monthValue
    date += "/"
    date += birthday.year
    return date
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun ShowUser() {

    Column (Modifier.background(DarkBlue)){
        val user = UserModel(1, "Danielito", LocalDateTime.now())
        Column(Modifier.padding(16.dp)) {
            UserCard(user)
        }
    }
}