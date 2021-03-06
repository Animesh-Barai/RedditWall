package com.mehul.redditwall.favorites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "fav_table")
class FavImage(@field:PrimaryKey(autoGenerate = true) var id: Int,
               @field:ColumnInfo(name = "fav_url") var favUrl: String,
               @field:ColumnInfo(name = "fav_gif") var isGif: Boolean,
               @field:ColumnInfo(name = "fav_post_link") var postLink: String,
               @SerializedName("fav_name")
               @field:ColumnInfo(name = "fav_name") var favName: String)