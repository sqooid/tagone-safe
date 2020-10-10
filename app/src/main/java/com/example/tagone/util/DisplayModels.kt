package com.example.tagone.util

import android.os.Parcelable
import com.example.tagone.database.PostDatabaseFormat
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DisplayModel(
    val id: Int?,
    val createdAt: String?,
    val source: String?,
    var localFavourite: Boolean = false,
    val tagStringGeneral: String,
    val tagStringCharacter: String,
    val tagStringCopyright: String,
    val tagStringArtist: String,
    val tagStringMeta: String,
    val fileUrl: String?,
    val previewFileUrl: String?
) : Parcelable

fun List<DisplayModel>.toPostDatabaseFormat(): List<PostDatabaseFormat> {
    return map {
        PostDatabaseFormat(
            id = it.id,
            createdAt = it.createdAt,
            source = it.source,
            tagStringGeneral = it.tagStringGeneral,
            tagStringArtist = it.tagStringArtist,
            tagStringMeta = it.tagStringMeta,
            tagStringCharacter = it.tagStringCharacter,
            tagStringCopyright = it.tagStringCopyright,
            fileUrl = it.fileUrl,
            previewFileUrl = it.previewFileUrl,
            localFavourite = it.localFavourite
        )
    }
}

fun DisplayModel.toPostDatabaseFormat(): PostDatabaseFormat {
    return PostDatabaseFormat(
        id = this.id,
        createdAt = this.createdAt,
        source = this.source,
        tagStringGeneral = this.tagStringGeneral,
        tagStringArtist = this.tagStringArtist,
        tagStringMeta = this.tagStringMeta,
        tagStringCharacter = this.tagStringCharacter,
        tagStringCopyright = this.tagStringCopyright,
        fileUrl = this.fileUrl,
        previewFileUrl = this.previewFileUrl,
        localFavourite = this.localFavourite
    )
}