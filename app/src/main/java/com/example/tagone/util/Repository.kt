package com.example.tagone.util

import android.app.Application
import android.util.Log
import android.webkit.DownloadListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.codekidlabs.storagechooser.utils.DiskUtil
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.example.tagone.database.PostsDatabase
import com.example.tagone.database.toDisplayModel
import com.example.tagone.network.DanbooruApi
import com.example.tagone.network.DanbooruTagNet
import com.example.tagone.network.toDisplayModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class PostsRepository(private val postsDatabase: PostsDatabase) {

    val favouritesList = Transformations.map(postsDatabase.postsDao.getByDate()) {
        it.toDisplayModel()
    }

    var remainingItemsInDownload = 0

    private val _searchList = MutableLiveData<List<DisplayModel>>()
    val searchList: LiveData<List<DisplayModel>>
        get() = _searchList

    private val _tagList = MutableLiveData<List<DanbooruTagNet>>()
    val tagList: LiveData<List<DanbooruTagNet>>
        get() = _tagList

    /**
     * Gets list of tags matching start of input
     */
    suspend fun getTagsFromNetwork(tag: String) {
        _tagList.value = DanbooruApi.retrofitService.getTags(10, 1, "$tag*", "count", true)
    }

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
        _searchList.value = _searchList.value?.plus(
            DanbooruApi.retrofitService.getPosts(tags, limit, page).toDisplayModel()
        )
    }

    /**
     * Function to download all favourites (avoids downloading duplicates)
     */
    fun downloadAllFavourites(application: Application, directoryString: String) {
        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .build()
        PRDownloader.initialize(application.applicationContext, config)

        // Getting names of files already existing in save directory
        val directory = File(directoryString)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val fileList = directory.listFiles()!!
        val files = if (fileList.isNotEmpty()) {
            fileList.map { it.name }
        } else {
            listOf()
        }

        // Iterating through list of posts in favourites list
        if (!favouritesList.value.isNullOrEmpty()) {
            for (post in favouritesList.value!!) {
                val fileName = createFileName(post.fileUrl!!)
                if (fileName !in files) {
                    Log.i("test", "Download initiated: $fileName")
                    downloadFile(post.fileUrl, directoryString, fileName)
                    remainingItemsInDownload++
                }
            }
        }
    }

    /**
     * Individual file download
     */
    private fun downloadFile(fileUrl: String?, directoryString: String?, fileName: String?): Int {
        val id = PRDownloader.download(
            fileUrl,
            directoryString,
            fileName
        )
            .build()
            .start(DownloadListener())
        Log.i("test","id: $id")
        return id
    }

    inner class DownloadListener : OnDownloadListener {
        override fun onDownloadComplete() {
            Log.i("test", "Download complete")
            remainingItemsInDownload--
            return
        }

        override fun onError(error: Error?) {
            return
        }
    }
}