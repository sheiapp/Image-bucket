package com.example.imagebucketapp.persistant_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Shaheer cs on 17/03/2022.
 */
@Entity(
    tableName = "image_data_table"
)
data class Entity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    val imageName: String,
    val imgData: ByteArray
)
