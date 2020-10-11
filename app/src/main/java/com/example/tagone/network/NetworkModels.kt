package com.example.tagone.network

import com.example.tagone.util.DisplayModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data class for post from api. Includes all meta-data
 */
@JsonClass(generateAdapter = true)
data class DanbooruPostNet(
    val id: Int?,
    val score: Int?,
    val source: String?,
    val rating: String?,
    @Json(name = "created_at") val createdAt: String?,
    @Json(name = "uploader_id") val uploaderId: String?,
    @Json(name = "fav_count") val favCount: Int?,
    @Json(name = "parent_id") val parentId: Int?,
    @Json(name = "pool_string") val poolString: String?,
    @Json(name = "is_favorited") val isFavorited: Boolean?,
    @Json(name = "tag_string_general") val tagStringGeneral: String,
    @Json(name = "tag_string_character") val tagStringCharacter: String,
    @Json(name = "tag_string_copyright") val tagStringCopyright: String,
    @Json(name = "tag_string_artist") val tagStringArtist: String,
    @Json(name = "tag_string_meta") val tagStringMeta: String,
    @Json(name = "file_url") val fileUrl: String?,
    @Json(name = "preview_file_url") val previewFileUrl: String?,
    @Json(name = "image_width") val imageWidth: Int,
    @Json(name = "image_height") val imageHeight: Int,
    @Json(name = "file_ext") val fileExt: String?
//    @Json(name = "large_file_url") val largeFileUrl: String,
//    val md5: String,
//    @Json(name = "last_comment_bumped_at") val lastCommentBumpedAt: String,
//    @Json(name = "tag_string") val tagString: String,
//    @Json(name = "is_note_locked") val isNoteLocked: Boolean,
//    @Json(name = "last_noted_at") val lastNotedAt: String,
//    @Json(name = "is_rating_locked") val isRatingLocked: Boolean,
//    @Json(name = "has_children") val hasChildren: Boolean,
//    @Json(name = "approver_id") val approverId: String,
//    @Json(name = "tag_count_general") val tagCountGeneral: Int,
//    @Json(name = "tag_count_artist") val tagCountArtist: Int,
//    @Json(name = "tag_count_character") val tagCountCharacter: Int,
//    @Json(name = "tag_count_copyright") val tagCountCopyright: Int,
//    @Json(name = "file_size") val fileSize: Int,
//    @Json(name = "is_status_locked") val isStatusLocked: Boolean,
//    @Json(name = "up_score") val upScore: Int,
//    @Json(name = "down_score") val downScore: Int,
//    @Json(name = "is_pending") val isPending: Boolean,
//    @Json(name = "is_flagged") val isFlagged: Boolean,
//    @Json(name = "is_deleted") val isDeleted: Boolean,
//    @Json(name = "tag_count") val tagCount: Int,
//    @Json(name = "updated_at") val updateAt: String,
//    @Json(name = "is_banned") val isBanned: Boolean,
//    @Json(name = "pixiv_id") val pixivId: Int,
//    @Json(name = "last_commented_at") val lastCommentedAt: String,
//    @Json(name = "has_active_children") val hasActiveChildren: Boolean,
//    @Json(name = "bit_flags") val bitFlags: Int,
//    @Json(name = "tag_count_meta") val tagCountMeta: Int,
//    @Json(name = "has_large") val hasLarge: Boolean?,
//    @Json(name = "has_visible_children") val hasVisibleChildren: Boolean,
)

/**
 * Extension function for conversion of network model to display model of post
 */
fun List<DanbooruPostNet>.toDisplayModel(): List<DisplayModel> {
    return map {
        DisplayModel(
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
            imageWidth = it.imageWidth,
            imageHeight = it.imageHeight,
            fileExt = it.fileExt
        )
    }
}

