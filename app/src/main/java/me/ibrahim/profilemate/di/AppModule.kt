package me.ibrahim.profilemate.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.ibrahim.profilemate.data.api.RemoteAPIs
import me.ibrahim.profilemate.data.managers.LocalDataStoreManagerImpl
import me.ibrahim.profilemate.data.repository.RemoteRepositoryImpl
import me.ibrahim.profilemate.domain.managers.ApiManager
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.domain.repository.RemoteRepository
import me.ibrahim.profilemate.domain.use_cases.LoginUseCase
import me.ibrahim.profilemate.domain.use_cases.ReadTokenUseCase
import me.ibrahim.profilemate.domain.use_cases.SaveTokenUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalDataStoreManager(app: Application): LocalDataStoreManager {
        return LocalDataStoreManagerImpl(app)
    }

    @Provides
    @Singleton
    fun provideReadTokenUseCase(localDataStoreManager: LocalDataStoreManager): ReadTokenUseCase =
        ReadTokenUseCase(localDataStoreManager = localDataStoreManager)

    @Provides
    @Singleton
    fun provideSaveTokenUseCase(localDataStoreManager: LocalDataStoreManager): SaveTokenUseCase =
        SaveTokenUseCase(localDataStoreManager = localDataStoreManager)

    @Provides
    @Singleton
    fun provideLoginUseCase(remoteRepository: RemoteRepository): LoginUseCase =
        LoginUseCase(remoteRepository = remoteRepository)


    @Provides
    @Singleton
    fun provideRemoteRepository(remoteAPIs: RemoteAPIs, apiManager: ApiManager): RemoteRepository {
        return RemoteRepositoryImpl(remoteAPIs = remoteAPIs, apiManager = apiManager)
    }
}