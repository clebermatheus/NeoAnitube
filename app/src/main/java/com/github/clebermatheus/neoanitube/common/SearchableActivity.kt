package com.github.clebermatheus.neoanitube.common

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.clebermatheus.neoanitube.R
import com.github.clebermatheus.neoanitube.anitube.constants.API
import com.github.clebermatheus.neoanitube.anitube.model.Subcategoria
import com.github.clebermatheus.neoanitube.anitube.viewmodels.AnimesViewAdapter
import com.github.clebermatheus.neoanitube.common.constants.Utils
import com.github.clebermatheus.neoanitube.common.constants.Utils.MAX_REQUESTS
import com.google.gson.Gson
import org.json.JSONObject

/**
 *
 * Acitivity para pesquisar animes
 *
 * Created by clebermatheus on 15/04/18.
 */
class SearchableActivity: AppCompatActivity() {
    private var requestQueue: RequestQueue? = null
    private lateinit var animesAdapter: AnimesViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)
        preferences = Preferences(this)
        animesAdapter = AnimesViewAdapter(preferences.getSubcategoria(Utils.PREF_ANIMES).SUBCATEGORIAS, this)
        recyclerView = findViewById<RecyclerView>(R.id.rvAnimes).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = animesAdapter
        }
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        this.intent = intent
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if(Intent.ACTION_SEARCH == intent!!.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            if(supportActionBar != null) {
                supportActionBar!!.title = query
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setHomeButtonEnabled(true)
            }
            val subcategoria = preferences.getSubcategoria(Utils.PREF_ANIMES).SUBCATEGORIAS
            if(!subcategoria.isEmpty()) {
                animesAdapter.clear().addAll(subcategoria.filter { it.name.contains(query, true) })
            } else { pesquisarAnimes(query) }
        }
    }

    private fun pesquisarAnimes(query: String) {
        if(requestQueue == null) requestQueue = Volley.newRequestQueue(this)
        val jsonRequest = JsonObjectRequest(API.SUBCATEGORIA+"anime", null, {
            val gson = Gson()
            val resultado: Subcategoria = gson.fromJson(it.toString(), Subcategoria::class.java)
            preferences.putSubcategoria(Utils.PREF_ANIMES, resultado)
            animesAdapter.clear().addAll(resultado.SUBCATEGORIAS.filter {
                it.name.contains(query, true)
            })
        }, { it.stackTrace })
        jsonRequest.retryPolicy = DefaultRetryPolicy(30000, MAX_REQUESTS, 1.0f)
        requestQueue!!.add<JSONObject>(jsonRequest)
    }

    companion object {
        const val TAG = "SearchableActivity"
    }
}