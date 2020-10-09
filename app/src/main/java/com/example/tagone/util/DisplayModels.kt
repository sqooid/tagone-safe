package com.example.tagone.util

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DisplayModel(
    val id: Int?,
    val createdAt: String?,
    val source: String?,
    val isFavorited: Boolean?,
    val tagStringGeneral: String,
    val tagStringCharacter: String,
    val tagStringCopyright: String,
    val tagStringArtist: String,
    val tagStringMeta: String,
    val fileUrl: String?) : Parcelable