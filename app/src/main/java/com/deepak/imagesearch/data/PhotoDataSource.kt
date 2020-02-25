package com.deepak.imagesearch.data

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.paging.PageKeyedDataSource
import com.deepak.imagesearch.models.FlickrPhoto
import com.deepak.imagesearch.network.FlickrApi

@SuppressLint("CheckResult")
class PhotoDataSource(
    private val flickrApi: FlickrApi,
    private val query: String,
    private val flickrPhotoViewModel: FlickrPhotoViewModel
) :
    PageKeyedDataSource<Int, FlickrPhoto>() {


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, FlickrPhoto>
    ) {
        try {
            flickrApi.search(query)
                .subscribe { response ->
                    if (response.photos.total.toInt() == 0) {
                        callback.onResult(response.photos.photo, 0, 0, null, null);
                    } else {
                        callback.onResult(
                            response.photos.photo,
                            response.photos.page * response.photos.perpage,
                            response.photos.total.toInt(),
                            null,
                            response.photos.page + 1
                        )
                    }
                }
        } catch (e: Exception) {
        }
    }


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, FlickrPhoto>) {
        try {
            flickrApi.search(query, params.key)
                .subscribe { response ->
                    var nextPage: Int? = null
                    if (response.photos.page < response.photos.pages) {
                        nextPage = response.photos.page + 1
                    }
                    callback.onResult(response.photos.photo, nextPage)
                }
        } catch (e: Exception) {
        }

    }


    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, FlickrPhoto>) {
        try {
            flickrApi.search(query, params.key)
                .subscribe { response ->
                    var previousPage: Int? = null
                    if (response.photos.page > 0) {
                        previousPage = response.photos.page - 1
                    }
                    callback.onResult(response.photos.photo, previousPage)
                }
        } catch (e: Exception) {

        }
    }
}
