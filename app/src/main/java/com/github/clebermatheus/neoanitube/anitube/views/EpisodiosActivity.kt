package com.github.clebermatheus.neoanitube.anitube.views

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request.Method.GET
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.github.clebermatheus.neoanitube.R
import com.github.clebermatheus.neoanitube.anitube.constants.API
import com.github.clebermatheus.neoanitube.anitube.model.Anime
import com.github.clebermatheus.neoanitube.anitube.model.Episodios
import com.github.clebermatheus.neoanitube.anitube.viewmodels.EpisodiosViewAdapter
import com.github.clebermatheus.neoanitube.common.constants.Utils.MAX_REQUESTS
import com.github.clebermatheus.neoanitube.common.models.GsonRequest
import com.google.gson.Gson

/**
 *
 * Activity que exibe todos os episódios do Anime
 *
 * Created by clebermatheus on 06/04/18.
 */
class EpisodiosActivity: AppCompatActivity() {
    private var requestQueue: RequestQueue? = null
    private lateinit var episodiosViewAdapter: EpisodiosViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episodios)

        recyclerView = findViewById(R.id.rvEpisodios)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        episodiosViewAdapter = EpisodiosViewAdapter(ArrayList())
        val anime: Anime = Gson().fromJson(intent.getStringExtra("anime"), Anime::class.java)
        val actionBar = supportActionBar

        if(actionBar != null){
            actionBar.title = anime.name
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        requestQueueEpisodios(anime)
        recyclerView.apply {
            setHasFixedSize(true)
            this.layoutManager = LinearLayoutManager(context)
            adapter = episodiosViewAdapter
        }
        swipeRefresh.setOnRefreshListener { requestQueueEpisodios(anime) }
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_dark)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_episodios, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        R.id.action_view -> {
            val ( episodios, tipoView) = episodiosViewAdapter
            item.icon = if(tipoView) {
                this.resources.getDrawable(R.drawable.ic_view_module_24dp, null)
            } else {
                this.resources.getDrawable(R.drawable.ic_view_list_24dp, null)
            }
            episodiosViewAdapter = EpisodiosViewAdapter(episodios, !tipoView)
            recyclerView.apply {
                layoutManager = if(!tipoView) {
                    GridLayoutManager(this.context, 4)
                } else { LinearLayoutManager(rootView.context) }
                adapter = episodiosViewAdapter
                setHasFixedSize(true)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun requestQueueEpisodios(anime: Anime) {
        if (requestQueue == null) requestQueue = Volley.newRequestQueue(this)

        val gsonRequest = GsonRequest<Episodios>(GET, API.EPISODIOS+anime.chid, Episodios::class.java,
                null, Response.Listener {
                    Log.d(TAG, it.toString())
                    episodiosViewAdapter.clear().addAll(it.EPISODIOS)
                    swipeRefresh.isRefreshing = false
                }, Response.ErrorListener { it.stackTrace })
        gsonRequest.retryPolicy = DefaultRetryPolicy(30000, MAX_REQUESTS, 1.0f)
        requestQueue!!.add(gsonRequest)
    }

    companion object {
        const val TAG = "EpisodiosActivity"
    }
}