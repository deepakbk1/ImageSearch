package com.deepak.imagesearch.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.deepak.imagesearch.R
import com.deepak.imagesearch.adapter.PhotosAdapter
import com.deepak.imagesearch.data.FlickrPhotoViewModel
import com.deepak.imagesearch.network.FlickrApi
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private val mHandler = Handler()
    private var mQueryString = ""

    companion object {
        private const val PARAM_QUERY: String = "PARAM_QUERY"
    }

    @Inject
    internal lateinit var flickrApi: FlickrApi
    private lateinit var photosAdapter: PhotosAdapter
    private lateinit var viewModel: FlickrPhotoViewModel

    private var currentQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidInjection.inject(this)

        photosAdapter = PhotosAdapter()
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = photosAdapter
        recyclerView.setHasFixedSize(true)

        viewModel = ViewModelProvider(this).get(FlickrPhotoViewModel::class.java)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PARAM_QUERY, currentQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentQuery = savedInstanceState.getString(PARAM_QUERY, null)
        fetchCurrentQuery()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (Intent.ACTION_SEARCH == intent?.action) {
            currentQuery = intent.getStringExtra(SearchManager.QUERY)
            fetchCurrentQuery(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val menuItem = menu?.findItem(R.id.app_bar_search)
        val searchView = menuItem?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //  searchView.setQuery(currentQuery, false)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mQueryString = newText!!

                mHandler.removeCallbacksAndMessages(null)

                mHandler.postDelayed(Runnable {
                    searchView.setQuery(mQueryString, true)
                }, 500)
                return false

            }
        })
        if (currentQuery != null) {
            menuItem.expandActionView()
            searchView.setQuery(currentQuery, false)
        }
        return true
    }

    private fun fetchCurrentQuery(resetData: Boolean = false) {
        if (currentQuery == null) return

        emptyScreenText.visibility = View.GONE
        searchLoading.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        viewModel.getPhotos(flickrApi, currentQuery!!, resetData)
            .observe(this, Observer { photos ->
                photosAdapter.submitList(photos)

                if (photos != null && photos.isNotEmpty()) {
                    searchLoading.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                } else {
                    emptyScreenText.visibility = View.VISIBLE
                    searchLoading.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                }
            })
    }
}
