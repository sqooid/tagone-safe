package com.example.tagone.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.tagone.database.FavouritesDatabase
import com.example.tagone.database.toDisplayModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostsRepository(private val favouritesDatabase: FavouritesDatabase) {

    val favouritesList = Transformations.map(favouritesDatabase.favouritesDao.getByDate()) {
        it.toDisplayModel()
    }

    suspend fun addToFavourites(post: DisplayModel) {
        withContext(Dispatchers.IO) {
            favouritesDatabase.favouritesDao.addToFavourites(post.toPostDatabaseFormat())
        }
    }

    suspend fun removeFromFavourites(post: DisplayModel) {
        withContext(Dispatchers.IO) {
            favouritesDatabase.favouritesDao.removeFromFavourites(post.toPostDatabaseFormat())
        }
    }
}