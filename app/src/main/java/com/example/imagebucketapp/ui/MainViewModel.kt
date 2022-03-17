package com.example.imagebucketapp.ui

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagebucketapp.persistant_data.Entity
import com.example.imagebucketapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject


/**
 * Created by Shaheer cs on 16/03/2022.
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val contentResolver: ContentResolver
) : ViewModel() {

    private val _searchImageResult = MutableStateFlow<Bitmap?>(null)
    val searchImageResult = _searchImageResult.asStateFlow()
    private val _messageToUser = MutableSharedFlow<String>()
    val messageToUser = _messageToUser.asSharedFlow()

    fun persistImageIntoDatabase(uri: Uri?) = viewModelScope.launch(Dispatchers.IO) {
        if (uri != null) {
            val bitmap: Bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            } else {
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream)
            val byteArrayInStringFormat = outputStream.toByteArray()
            getImageFileName(uri)?.let {
                val id = mainRepository.persistImageInDatabase(
                    Entity(
                        imgData = byteArrayInStringFormat,
                        imageName = it
                    )
                )
                mainRepository.getPersistedImageDataWithId(id).catch {
                    _messageToUser.emit(ERROR_TOO_BIG_EXCEPTION)
                }.collectLatest { entity ->
                    val byteArray = entity.imgData
                    _searchImageResult.value =
                        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                }
            }
        } else {
            _messageToUser.emit(ERROR_MESSAGE_FOR_URI_WHEN_IT_NULL)
        }
    }

    private fun getImageFileName(uri: Uri?): String? {
        uri?.let { returnUri ->
            contentResolver.query(returnUri, null, null, null, null)
        }?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            return cursor.getString(nameIndex).substringBefore(".")
        }
        return null
    }


    fun getPersistedImageWithFileName(_fileName: String) = viewModelScope.launch(Dispatchers.IO) {
        if (_fileName.isNotEmpty())
            if (mainRepository.isImageIsExistWithThisFileName(_fileName)) {
                mainRepository.getPersistedImageWithFileName(_fileName).catch {
                    _messageToUser.emit(ERROR_TOO_BIG_EXCEPTION)
                }.collect {
                    val byteArray = it.imgData
                    _searchImageResult.value =
                        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                }
            } else {
                _messageToUser.emit(ERROR_MESSAGE_FOR_SEARCH_RESULT)
            }
    }

    companion object {
        const val ERROR_MESSAGE_FOR_SEARCH_RESULT =
            "Oops! The image name which you searched not found in DB"
        const val ERROR_MESSAGE_FOR_URI_WHEN_IT_NULL =
            "Oops! the image you selected has some issue please try with different image file"
        const val ERROR_TOO_BIG_EXCEPTION =
            "Oops! the image is too big to handle. please try with some samall images"
    }
}