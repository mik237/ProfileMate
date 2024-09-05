package me.ibrahim.profilemate.di

import android.app.Application
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.ibrahim.profilemate.data.api.AuthInterceptor
import me.ibrahim.profilemate.data.api.RemoteAPIs
import me.ibrahim.profilemate.data.api.RemoteAPIsLocalImpl
import me.ibrahim.profilemate.data.managers.ApiManagerImpl
import me.ibrahim.profilemate.domain.managers.ApiManager
import me.ibrahim.profilemate.domain.managers.ConnectionManager
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRemoteAPIs(okHttpClient: OkHttpClient): RemoteAPIs {
        //Providing local implementation of RemoteAPIs with mock response.
        return RemoteAPIsLocalImpl()

        /*
        val retrofit = Retrofit.Builder()
            .baseUrl("BASE_URL")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(RemoteAPIs::class.java)
        */
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
        localDataStoreManager: LocalDataStoreManager,
        connectionManager: ConnectionManager,
        gson: Gson
    ): ApiManager {
        return ApiManagerImpl(localDataStoreManager = localDataStoreManager, connectionManager = connectionManager, gson = gson)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()


    @Provides
    @Singleton
    fun provideConnectionManager(app: Application) = ConnectionManager(app)


    /* @Provides
     @Singleton
     fun provideMockServer(): MockWebServer {
         val mockServer = MockWebServer()
         mockServer.enqueue(MockResponse().setBody("{ \"userid\": \"12345\", \"token\": \"abcdef\" }"))
         mockServer.enqueue(MockResponse().setBody("{ \"email\": \"user@example.com\", \"avatar_url\": \"https://example.com/avatar.png\" }"))
         mockServer.enqueue(MockResponse().setBody("{ \"avatar_url\": \"https://example.com/new_avatar.png\" }"))
         return mockServer
     }*/

}