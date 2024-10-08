package me.ibrahim.profilemate.domain.use_cases.main

import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(val localDataStoreManager: LocalDataStoreManager) {
    suspend operator fun invoke(token: String) {
        localDataStoreManager.saveToken(token)
    }
}