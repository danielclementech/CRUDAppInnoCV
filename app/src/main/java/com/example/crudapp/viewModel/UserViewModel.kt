package com.example.crudapp.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crudapp.business.UserInteractor
import com.example.crudapp.data.CRUDApi
import com.example.crudapp.data.Mapper
import com.example.crudapp.model.UserModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

enum class Status {
    LOADING,
    SUCCESS,
    FAILED
}

@RequiresApi(Build.VERSION_CODES.O)
@InternalCoroutinesApi
class UserViewModel : ViewModel() {

    private val mapper = Mapper()

    val retrofitBuilder = Retrofit.Builder()
        .baseUrl("https://hello-world.innocv.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CRUDApi::class.java)

    private var _usersList = MutableLiveData<List<UserModel>>()
    val usersList: LiveData<List<UserModel>>
        get() = _usersList

    private var _loadingUsers = MutableLiveData<Status>()
    val loadingUsers: LiveData<Status>
        get() = _loadingUsers

    init {
        _loadingUsers.postValue(Status.LOADING)
        loadData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadData() {
        viewModelScope.launch {
            val data = retrofitBuilder.getUsers()
            _loadingUsers.postValue(Status.LOADING)

            if (data.isSuccessful) {
                _usersList.postValue(data.body()?.let { mapper.mapUsers(it) })
                _loadingUsers.postValue(Status.SUCCESS)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun postUser(user: UserModel) {
        viewModelScope.launch {
            val user = mapper.mapUserToResponse(user)
            retrofitBuilder.postUser(user)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteUser(id: Int) {
        viewModelScope.launch {
            retrofitBuilder.deleteUser(id)
        }
    }

    fun updateUser(user: UserModel) {
        viewModelScope.launch {
            retrofitBuilder.updateUser(mapper.mapUserToResponse(user))
        }
    }
}