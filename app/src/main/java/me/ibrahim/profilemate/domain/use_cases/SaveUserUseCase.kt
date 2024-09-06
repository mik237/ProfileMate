package me.ibrahim.profilemate.domain.use_cases

import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.domain.models.User
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(val localDataStoreManager: LocalDataStoreManager) {

    suspend operator fun invoke(user: User) {
        localDataStoreManager.saveUser(user)
    }

}