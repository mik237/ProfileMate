package me.ibrahim.profilemate.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.domain.use_cases.ReadTokenUseCase
import javax.inject.Singleton


@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

//    @Provides
//    fun provideReadTokenUseCase(
//        localDataStoreManager: LocalDataStoreManager
//    ): ReadTokenUseCase =
//        ReadTokenUseCase(
//            localDataStoreManager = localDataStoreManager,
//        )

}