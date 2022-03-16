package com.example.imagebucketapp.persistant_data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by Shaheer cs on 16/03/2022.
 */
@Database(entities = [ImageEntity::class], version = 1, exportSchema = false)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun getImageDao(): ImageDao

    companion object {
        const val DATABASE_NAME="image_database"
    }
}