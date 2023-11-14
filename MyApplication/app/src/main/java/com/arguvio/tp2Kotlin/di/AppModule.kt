package com.arguvio.tp2Kotlin.di

import android.content.Context
import androidx.room.Room
import com.arguvio.tp2Kotlin.OnlineSource.OnlineSource
import com.arguvio.tp2Kotlin.api.ApiService
import com.arguvio.tp2Kotlin.dao.UserDao
import com.arguvio.tp2Kotlin.database.AppDatabase
import com.arguvio.tp2Kotlin.repository.UserRepository
import com.arguvio.tp2Kotlin.viewModel.UserViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideUserRepository(onlineSource: OnlineSource, userDao: UserDao): UserRepository {
        return UserRepository(onlineSource, userDao)
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

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "appDatabase"
        ).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
}