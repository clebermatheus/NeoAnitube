package com.github.clebermatheus.neoanitube.anitube.views

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.clebermatheus.neoanitube.R
import com.github.clebermatheus.neoanitube.anitube.constants.API
import com.github.clebermatheus.neoanitube.anitube.model.Anime
import com.github.clebermatheus.neoanitube.anitube.model.Episodio
import com.github.clebermatheus.neoanitube.anitube.viewmodels.EpisodiosViewAdapter
import com.github.clebermatheus.neoanitube.common.constants.Utils.MAX_REQUESTS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun requestQueueEpisodios(anime: Anime) {
        if (requestQueue == null) requestQueue = Volley.newRequestQueue(this)

        val jsonRequest = JsonObjectRequest(API.EPISODIOS+anime.chid, null, {
            val gson = Gson()
            val type: Type = object : TypeToken<ArrayList<Episodio>>() {}.type
            val resultado: ArrayList<Episodio> = gson.fromJson(it.getString("EPISODIOS"), type)
            Log.d(TAG, resultado.toString())
            episodiosViewAdapter.clear().addAll(resultado)
            swipeRefresh.isRefreshing = false
        }, { it.stackTrace })
        jsonRequest.retryPolicy = DefaultRetryPolicy(30000, MAX_REQUESTS, 1.0f)
        requestQueue!!.add<JSONObject>(jsonRequest)
    }

    companion object {
        const val TAG = "EpisodiosActivity"
    }
}