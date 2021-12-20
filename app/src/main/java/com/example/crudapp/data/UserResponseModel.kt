package com.example.crudapp.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.type.DateTime

data class UserResponseModel(

    @SerializedName("id")
    var userId : Int,

    @SerializedName("name")
    @Expose
    var name : String? = null,

    @SerializedName("birthdate")
    @Expose
    var birthdate : String? = null,

)