package com.example.degreewiki.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

interface DataRepository {
    val data: Flow<List<String>>
}

@Singleton
class DefaultDataRepository @Inject constructor() : DataRepository {
    override val data: Flow<List<String>> = flow {
        emit(listOf("Android Jetpack Compose", "Dagger Hilt", "Room Database", "Retrofit"))
    }
}
