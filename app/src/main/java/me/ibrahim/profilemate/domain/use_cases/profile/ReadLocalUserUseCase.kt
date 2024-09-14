package me.ibrahim.profilemate.domain.use_cases.profile

import kotlinx.coroutines.flow.Flow
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.domain.models.User
import javax.inject.Inject

class ReadLocalUserUseCase @Inject constructor(val localDataStoreManager: LocalDataStoreManager) {
    operator fun invoke(): Flow<User?> {
        return localDataStoreManager.readUser()
    }
}