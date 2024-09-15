package me.ibrahim.profilemate.di

import android.app.Application
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.ibrahim.profilemate.data.managers.LocalDataStoreManagerImpl
import me.ibrahim.profilemate.data.managers.SessionManagerImpl
import me.ibrahim.profilemate.data.remote.RemoteAPIs
import me.ibrahim.profilemate.data.repository.RemoteRepositoryImpl
import me.ibrahim.profilemate.data.utils.DefaultDispatchersProvider
import me.ibrahim.profilemate.domain.managers.ApiManager
import me.ibrahim.profilemate.domain.managers.ConnectionManager
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.domain.managers.SessionManager
import me.ibrahim.profilemate.domain.repository.RemoteRepository
import me.ibrahim.profilemate.domain.use_cases.login.LoginUseCase
import me.ibrahim.profilemate.domain.utils.DispatchersProvider
import me.ibrahim.profilemate.utils.FileUtil
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalDataStoreManager(app: Application, gson: Gson): LocalDataStoreManager {
        return LocalDataStoreManagerImpl(context = app, gson = gson)
    }


    @Provides
    @Singleton
    fun provideRemoteRepository(
        remoteAPIs: RemoteAPIs,
        apiManager: ApiManager,
        sessionManager: SessionManager,
        connectionManager: ConnectionManager,
        fileUtil: FileUtil
    ): RemoteRepository {
        return RemoteRepositoryImpl(
            remoteAPIs = remoteAPIs,
            apiManager = apiManager,
            sessionManager = sessionManager,
            connectionManager = connectionManager,
            fileUtil = fileUtil
        )
    }

    @Provides
    @Singleton
    fun provideSessionManager(localDataStoreManager: LocalDataStoreManager): SessionManager {
        return SessionManagerImpl(localDataStoreManager)
    }

    @Provides
    @Singleton
    fun provideDispatchersProvider(): DispatchersProvider = DefaultDispatchersProvider()

}