package com.jgcoding.kotlin.commercelistapp.di

import android.app.Application
import androidx.room.Room
import com.jgcoding.kotlin.commercelistapp.BuildConfig.BASE_URL
import com.jgcoding.kotlin.commercelistapp.data.RepositoryImpl
import com.jgcoding.kotlin.commercelistapp.data.database.CommerceDatabase
import com.jgcoding.kotlin.commercelistapp.data.database.Converter
import com.jgcoding.kotlin.commercelistapp.data.database.dao.CommerceDao
import com.jgcoding.kotlin.commercelistapp.data.network.ApiService
import com.jgcoding.kotlin.commercelistapp.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Podría haber creado 2 módulos independientes. Uno para la BBDD y otro para la API. Pero en este caso no es necesario.
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): CommerceDatabase =
        Room.databaseBuilder(app, CommerceDatabase::class.java, "commerce_database")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideCommerceDao(db: CommerceDatabase) = db.getCommerceDao()

    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService, dao: CommerceDao): Repository =
        RepositoryImpl(apiService, dao)
}