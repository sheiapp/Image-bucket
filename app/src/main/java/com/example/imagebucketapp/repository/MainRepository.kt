package com.example.imagebucketapp.repository

import com.example.imagebucketapp.persistant_data.Entity
import com.example.imagebucketapp.persistant_data.ImageDatabase
import javax.inject.Inject

/**
 * Created by Shaheer cs on 16/03/2022.
 */
class MainRepository @Inject constructor(private val imageDatabase: ImageDatabase) {

    suspend fun persistImageInDatabase(imageEntity: Entity) =
        imageDatabase.getImageDao().persistImage(imageEntity)

    fun getPersistedImageWithFileName(fileName: String) =
        imageDatabase.getImageDao().getPersistedImageDataWithFileName(fileName)

    fun isImageIsExistWithThisFileName(fileName: String) =
        imageDatabase.getImageDao().isImageIsExistWithThisFileName(fileName)

     fun getPersistedImageDataWithId(id:Long)=imageDatabase.getImageDao().getPersistedImageDataWithId(id)
}