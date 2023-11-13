package com.arguvio.tp2Kotlin.di

import com.arguvio.tp2Kotlin.OnlineSource.OnlineSource
import com.arguvio.tp2Kotlin.api.ApiService
import com.arguvio.tp2Kotlin.repository.UserRepository
import com.arguvio.tp2Kotlin.viewModel.UserViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://64ff643ff8b9eeca9e2a1214.mockapi.io/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object OnlineSourceModule {

    @Provides
    fun provideOnlineSource(apiService: ApiService): OnlineSource {
        return OnlineSource(apiService)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {

    @Provides
    fun provideUserRepository(onlineSource: OnlineSource): UserRepository {
        return UserRepository(onlineSource)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {

    @Provides
    @Singleton
    fun provideUserViewModel(userRepository: UserRepository): UserViewModel {
        return UserViewModel(userRepository)
    }
}