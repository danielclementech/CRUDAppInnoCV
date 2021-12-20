package com.example.crudapp.model

import com.google.type.DateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class UserModel(
    val id: Int,
    var name: String? = null,
    var birthdate: LocalDateTime? = null,
)