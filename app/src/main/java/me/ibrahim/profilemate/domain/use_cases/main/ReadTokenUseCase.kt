package me.ibrahim.profilemate.domain.use_cases.main

import kotlinx.coroutines.flow.Flow
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import javax.inject.Inject

class ReadTokenUseCase @Inject constructor(private val localDataStoreManager: LocalDataStoreManager) {

    operator fun invoke(): Flow<String> = localDataStoreManager.readToken()

}