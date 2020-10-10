package com.example.tagone.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavouritesDao {
    @Query("SELECT * FROM favourites_table ORDER BY dateFavourited")
    fun getByDate(): LiveData<List<PostDatabaseFormat>>

    @Insert
    fun addToFavourites(post: PostDatabaseFormat)

    @Delete
    fun removeFromFavourites(post: PostDatabaseFormat)
}