package com.booboot.vndbandroid.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.ui.login.LoginActivity
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.progress_bar.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView {
    @Inject
    lateinit var presenter: MainPresenter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        (application as App).appComponent.inject(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        if (!Preferences.loggedIn) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
//        }

        presenter.attachView(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        /* Filter */
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_filter).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.action_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val restaurantId = query.trim().toIntOrNull()
                if (restaurantId != null) {
                    presenter.loadRestaurant()
                    searchView.isIconified = true
                    searchView.isIconified = true
                }
                return false
            }

            override fun onQueryTextChange(search: String): Boolean {
                return true
            }
        })

        searchView.clearFocus()

        val searchIconId = searchView.context.resources.getIdentifier("android:id/search_button", null, null)
        val searchIcon = searchView.findViewById<ImageView>(searchIconId)
        searchIcon.setImageResource(R.drawable.ic_search_white_24dp)

        return true
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) VISIBLE else GONE
    }
}