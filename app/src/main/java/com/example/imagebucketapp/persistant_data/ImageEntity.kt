package com.example.imagebucketapp.persistant_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Shaheer cs on 16/03/2022.
 */
@Entity(
    tableName = "image_table"
)
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "image_file_name")
    val imageFileName: String,
    @ColumnInfo(name = "image")
    val image: String
)