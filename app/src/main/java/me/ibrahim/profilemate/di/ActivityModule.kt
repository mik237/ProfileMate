package me.ibrahim.profilemate.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


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