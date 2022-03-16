package com.example.imagebucketapp.persistant_data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by Shaheer cs on 16/03/2022.
 */
@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun persistImage(imageEntity: ImageEntity): Flow<Int>

    @Query("SELECT * FROM image_table WHERE image_file_name=:fileName")
    fun getPersistedImageDataWithFileName(fileName: String): Flow<String>
}