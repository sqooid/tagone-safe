package com.example.tagone.util

import android.os.Parcelable
import com.example.tagone.database.FavouritesDatabaseFormat
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DisplayModel(
    val domain: Int,
    val fileUrl: String?,
    val id: Int?,
    val createdAt: String?,
    val source: String?,
    val tagStringGeneral: String,
    val tagStringCharacter: String,
    val tagStringCopyright: String,
    val tagStringArtist: String,
    val tagStringMeta: String,
    val previewFileUrl: String?,
    val sampleFileUrl: String?,
    val imageWidth: Int,
    val imageHeight: Int,
    val fileExt: String?,
) : Parcelable

fun List<DisplayModel>.toFavouritesDatabaseFormat(): List<FavouritesDatabaseFormat> {
    return map {
        FavouritesDatabaseFormat(
            id = it.id,
            createdAt = it.createdAt,
            source = it.source,
            tagStringGeneral = it.tagStringGeneral,
            tagStringArtist = it.tagStringArtist,
            tagStringMeta = it.tagStringMeta,
            tagStringCharacter = it.tagStringCharacter,
            tagStringCopyright = it.tagStringCopyright,
            fileUrl = it.fileUrl ?: "",
            previewFileUrl = it.previewFileUrl,
            imageHeight = it.imageHeight,
            imageWidth = it.imageWidth,
            fileExt = it.fileExt,
            sampleFileUrl = it.sampleFileUrl ?: "",
            domain = it.domain,
        )
    }
}

fun DisplayModel.toFavouritesDatabaseFormat(): FavouritesDatabaseFormat {
    return FavouritesDatabaseFormat(
        domain = this.domain,
        sampleFileUrl = this.sampleFileUrl ?: "",
        id = this.id,
        createdAt = this.createdAt,
        source = this.source,
        tagStringGeneral = this.tagStringGeneral,
        tagStringArtist = this.tagStringArtist,
        tagStringMeta = this.tagStringMeta,
        tagStringCharacter = this.tagStringCharacter,
        tagStringCopyright = this.tagStringCopyright,
        fileUrl = this.fileUrl  ?: "",
        previewFileUrl = this.previewFileUrl,
        imageWidth = this.imageWidth,
        imageHeight = this.imageHeight,
        fileExt = this.fileExt
    )
}