package me.ibrahim.profilemate.domain.use_cases

import kotlinx.coroutines.flow.Flow
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager

class ReadTokenUseCase(private val localDataStoreManager: LocalDataStoreManager) {

    operator fun invoke(): Flow<String> = localDataStoreManager.readToken()

}