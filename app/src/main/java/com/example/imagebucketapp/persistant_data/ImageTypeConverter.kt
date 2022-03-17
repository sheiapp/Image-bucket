package com.example.imagebucketapp.persistant_data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * Created by Shaheer cs on 17/03/2022.
 */
object ImageTypeConverter {
    @TypeConverter
    @JvmStatic
    fun fromString(value: String): ByteArray {
        val byteType = object : TypeToken<ByteArray>() {}.type
        return Gson().fromJson(value, byteType)
    }

    @TypeConverter
    @JvmStatic
    fun fromList(byteType: ByteArray): String {
        val gson = Gson()
        return gson.toJson(byteType)
    }
}