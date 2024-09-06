package me.ibrahim.profilemate.di

import android.app.Application
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.ibrahim.profilemate.data.managers.LocalDataStoreManagerImpl
import me.ibrahim.profilemate.data.remote.RemoteAPIs
import me.ibrahim.profilemate.data.repository.RemoteRepositoryImpl
import me.ibrahim.profilemate.domain.managers.ApiManager
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.domain.repository.RemoteRepository
import me.ibrahim.profilemate.domain.use_cases.LoginUseCase
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalDataStoreManager(app: Application, gson: Gson): LocalDataStoreManager {
        return LocalDataStoreManagerImpl(context = app, gson = gson)
    }


    /*@Provides
    @Singleton
    fun provideSaveUserUseCase(
        localDataStoreManager: LocalDataStoreManager
    ): SaveUserUseCase =
        SaveUserUseCase(
            localDataStoreManager = localDataStoreManager
        )*/

    /*@Provides
    @Singleton
    fun provideReadUserUseCase(
        localDataStoreManager: LocalDataStoreManager
    ): ReadUserUseCase = ReadUserUseCase(
        localDataStoreManager = localDataStoreManager
    )*/

    @Provides
    @Singleton
    fun provideLoginUseCase(remoteRepository: RemoteRepository): LoginUseCase =
        LoginUseCase(remoteRepository = remoteRepository)


    @Provides
    @Singleton
    fun provideRemoteRepository(remoteAPIs: RemoteAPIs, apiManager: ApiManager, mockWebServer: MockWebServer): RemoteRepository {
        return RemoteRepositoryImpl(remoteAPIs = remoteAPIs, apiManager = apiManager)
    }
}