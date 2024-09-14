package me.ibrahim.profilemate.di

import android.app.Application
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.ibrahim.profilemate.data.remote.ResponseBuilder
import me.ibrahim.profilemate.data.managers.ApiManagerImpl
import me.ibrahim.profilemate.data.managers.ConnectionManagerImpl
import me.ibrahim.profilemate.data.remote.AuthInterceptor
import me.ibrahim.profilemate.data.remote.RemoteAPIs
import me.ibrahim.profilemate.data.remote.RemoteAPIsLocalImpl
import me.ibrahim.profilemate.domain.managers.ApiManager
import me.ibrahim.profilemate.domain.managers.ConnectionManager
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRemoteAPIs(
        okHttpClient: OkHttpClient,
        mockServer: MockWebServer,
        responseBuilder: ResponseBuilder
    ): RemoteAPIs {

        //Providing local implementation of RemoteAPIs with mock response.
        return RemoteAPIsLocalImpl(responseBuilder = responseBuilder)

        /*return runBlocking {
            val retrofit = withContext(Dispatchers.IO) {
                Retrofit.Builder()
                    .baseUrl(mockServer.url("/"))
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            retrofit.create(RemoteAPIs::class.java)
        }*/
    }


    @Provides
    @Singleton
    fun provideAuthInterceptor(localDataStoreManager: LocalDataStoreManager): AuthInterceptor {
        return AuthInterceptor(localDataStoreManager = localDataStoreManager)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideApiManager(
        gson: Gson
    ): ApiManager {
        return ApiManagerImpl()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()


    @Provides
    @Singleton
    fun provideConnectionManager(app: Application): ConnectionManager = ConnectionManagerImpl(app)


    @Provides
    @Singleton
    fun provideMockServer(): MockWebServer {
        val mockServer = MockWebServer()
        return mockServer
    }

}