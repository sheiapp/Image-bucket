package com.example.imagebucketapp.persistant_data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

/**
 * Created by Shaheer cs on 16/03/2022.
 */
@Dao
interface ImageDao {

    @Query("SELECT EXISTS(SELECT * FROM image_data_table WHERE imageName=:imageName )")
    fun isImageIsExistWithThisFileName(imageName: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun persistImage(imageEntity: Entity): Long

    @Query("SELECT * FROM image_data_table WHERE imageName=:fileName")
     fun getPersistedImageDataWithFileName(fileName: String): Flow<Entity>

    @Query("SELECT * FROM image_data_table WHERE id=:id")
     fun getPersistedImageDataWithId(id: Long): Flow<Entity>
}