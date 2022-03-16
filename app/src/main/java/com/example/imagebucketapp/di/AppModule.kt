package com.example.imagebucketapp.di

import android.content.Context
import androidx.room.Room
import com.example.imagebucketapp.persistant_data.ImageDatabase
import com.example.imagebucketapp.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Shaheer cs on 16/03/2022.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun weatherDatabaseProvider(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ImageDatabase::class.java, ImageDatabase.DATABASE_NAME)
            .build()

    @Provides
    @Singleton
    fun weatherDaoProvider(dataBase: ImageDatabase) = dataBase.getImageDao()

    @Provides
    @Singleton
    fun mainRepositoryProvider(imageDatabase: ImageDatabase) = MainRepository(imageDatabase)

}