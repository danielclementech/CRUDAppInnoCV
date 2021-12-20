package com.example.crudapp.data

import retrofit2.Response
import retrofit2.http.*

interface CRUDApi {

    /**
     * GET User
     */
    @GET("/api/User/{id}")
    suspend fun getUser(
        @Path("id") id: Int
    ): Response<UserResponseModel>

    /**
     * GET Users
     */
    @GET("/api/User")
    suspend fun getUsers(): Response<ArrayList<UserResponseModel>>

    /**
     * POST User
     */
    @POST("/api/User")
    suspend fun postUser(@Body User: UserResponseModel): Response<Void>

    /**
     * PUT User
     */
    @PUT("/api/User")
    suspend fun updateUser(@Body user: UserResponseModel): Response<Void>

    /**
     * DELETE User
     */
    @DELETE("/api/User/{id}")
    suspend fun deleteUser(
        @Path("id") id: Int
    ): Response<Void>
}