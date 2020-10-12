package com.example.tagone.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.tagone.database.PostsDatabase
import com.example.tagone.database.toDisplayModel
import com.example.tagone.network.DanbooruApi
import com.example.tagone.network.toDisplayModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostsRepository(private val postsDatabase: PostsDatabase) {

    val favouritesList = Transformations.map(postsDatabase.postsDao.getByDate()) {
        it.toDisplayModel()
    }

    private val _searchList = MutableLiveData<List<DisplayModel>>()
    val searchList: LiveData<List<DisplayModel>>
        get() = _searchList

    /**
     * Favourites database manipulation
     */
    suspend fun addToFavourites(post: DisplayModel) {
        withContext(Dispatchers.IO) {
            postsDatabase.postsDao.addToFavourites(post.toFavouritesDatabaseFormat())
        }
    }

    suspend fun removeFromFavourites(post: DisplayModel) {
        withContext(Dispatchers.IO) {
            postsDatabase.postsDao.removeFromFavourites(post.toFavouritesDatabaseFormat())
        }
    }

    fun isFavourited(fileUrl: String): LiveData<Boolean> {
        return postsDatabase.postsDao.isInFavourites(fileUrl)
    }

    /**
     * Tag search network retrieval functions
     */
    suspend fun getPostsFromNetwork(tags: String, limit: Int, page: Int) {
        _searchList.value = DanbooruApi.retrofitService.getPosts(tags, limit, page).toDisplayModel()
    }

    suspend fun addPostsFromNetwork(tags: String, limit: Int, page: Int) {
        _searchList.value = _searchList.value?.plus(DanbooruApi.retrofitService.getPosts(tags, limit, page).toDisplayModel())
    }
}