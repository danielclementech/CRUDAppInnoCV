package com.example.crudapp.business

import com.example.crudapp.data.CRUDApi
import com.example.crudapp.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserInteractorImpl() : UserInteractor {


    override suspend fun getUser(): Flow<UserModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(): Flow<List<UserModel>> {
        TODO("Not yet implemented")
    }
}