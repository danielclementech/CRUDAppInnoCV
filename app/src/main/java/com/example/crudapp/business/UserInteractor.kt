package com.example.crudapp.business

import com.example.crudapp.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserInteractor {

    suspend fun getUser(): Flow<UserModel?>

    suspend fun getAllUsers(): Flow<List<UserModel>>

}