package com.example.imagebucketapp.repository

import com.example.imagebucketapp.persistant_data.ImageDatabase
import com.example.imagebucketapp.persistant_data.ImageEntity
import javax.inject.Inject

/**
 * Created by Shaheer cs on 16/03/2022.
 */
class MainRepository @Inject constructor(private val imageDatabase: ImageDatabase) {

    suspend fun persistImageInDatabase(imageEntity: ImageEntity) =
        imageDatabase.getImageDao().persistImage(imageEntity)

    fun getPersistedImageWithFileName(fileName: String) =
        imageDatabase.getImageDao().getPersistedImageDataWithFileName(fileName)
}