package com.example.imagebucketapp.ui

import androidx.lifecycle.ViewModel
import com.example.imagebucketapp.persistant_data.ImageEntity
import com.example.imagebucketapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Shaheer cs on 16/03/2022.
 */

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    suspend fun persistImageIntoDatabase(_image: String, _fileName: String)=
        mainRepository.persistImageInDatabase(ImageEntity(image=_image,imageFileName = _fileName))


    fun getPersistedImageWithFileName(_fileName:String)=mainRepository.getPersistedImageWithFileName(_fileName)
}