package me.ibrahim.profilemate.domain.use_cases

import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager

class SaveTokenUseCase(val localDataStoreManager: LocalDataStoreManager) {

    suspend operator fun invoke(token: String) {
        localDataStoreManager.saveToken(token)
    }

}