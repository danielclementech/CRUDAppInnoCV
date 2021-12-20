package com.example.crudapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.crudapp.model.UserModel
import java.time.LocalDateTime
import java.util.*

class Mapper {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapUsers(users: ArrayList<UserResponseModel>): List<UserModel> {
        val list = mutableListOf<UserModel>()
        for (user in users) {
            list.add(mapUser(user))
        }
        return list
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapUser(user: UserResponseModel): UserModel {
        return UserModel(
            user.userId,
            user.name?: "",
            LocalDateTime.parse(user.birthdate)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapUserToResponse(user: UserModel): UserResponseModel {
        return UserResponseModel(
            user.id,
            user.name?: "",
            user.birthdate.toString()
            )
    }
}