package com.deepak.imagesearch.network

import android.net.Uri
import com.deepak.imagesearch.models.FlickrSearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {

    @GET("services/rest/?method=flickr.photos.search&format=json&nojsoncallback=1")
    fun search(
        @Query("text") input: String,
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = getFlickApiKey()
    ): Single<FlickrSearchResponse>
}

/**
 * Helper method to build the Flick photo URL base.
 */
fun getPhotoUrl(farm: Int, server: String, id: String, secret: String): Uri {
    return Uri.parse(
        String.format(
            "http://farm%d.static.flickr.com/%s/%s_%s.jpg",
            farm,
            server,
            id,
            secret
        )
    )
}

fun getBaseUrl(): String {
    return "https://api.flickr.com"
}

/**
 * Returns the API KEY used for authentication with the Flickr API.
 */
fun getFlickApiKey(): String {
    return "0f924b73fabbb286c1d1e01dc763d18a"
}
