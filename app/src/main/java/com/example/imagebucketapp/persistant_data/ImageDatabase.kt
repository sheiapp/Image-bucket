package com.example.imagebucketapp.persistant_data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Created by Shaheer cs on 16/03/2022.
 */
@Database(entities = [Entity::class], version = 1, exportSchema = false)
@TypeConverters(ImageTypeConverter::class)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun getImageDao(): ImageDao

    companion object {
        const val DATABASE_NAME = "image_database"
    }
}