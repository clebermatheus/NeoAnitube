package com.github.clebermatheus.neoanitube.anitube.views

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.clebermatheus.neoanitube.R
import com.github.clebermatheus.neoanitube.anitube.model.Subcategoria
import com.github.clebermatheus.neoanitube.anitube.viewmodels.AnimesViewAdapter
import com.github.clebermatheus.neoanitube.anitube.constants.API
import com.github.clebermatheus.neoanitube.common.constants.Utils.MAX_REQUESTS
import com.google.gson.Gson
import org.json.JSONObject

/**
 * Fragmento de Animes
 *
 * Created by clebermatheus on 27/03/18.
 */
class AnimesFragment : Fragment() {
    private var requestQueue: RequestQueue? = null
    private lateinit var animesAdapter: AnimesViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var txtError: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_animes, container, false)
        animesAdapter = AnimesViewAdapter(ArrayList())
        txtError = rootView.findViewById(R.id.txt_anime_not_found) as TextView
        swipeRefresh = rootView.findViewById(R.id.swipeRefresh) as SwipeRefreshLayout
        this.requestQueueAnimes(rootView.context)
        recyclerView = rootView.findViewById<RecyclerView>(R.id.animeView).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(rootView.context)
            adapter = animesAdapter
        }
        verifyAdapterIsEmpty()
        swipeRefresh.setOnRefreshListener { requestQueueAnimes(rootView.context) }
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_dark)
        return rootView
    }

    private fun requestQueueAnimes(context: Context) {
        if (requestQueue == null) requestQueue = Volley.newRequestQueue(context)

        val jsonRequest = JsonObjectRequest(API.SUBCATEGORIA+"anime", null, {
            val gson = Gson()
            val resultado: Subcategoria = gson.fromJson(it.toString(), Subcategoria::class.java)
            Log.d(TAG, resultado.toString())
            animesAdapter.clear()
            animesAdapter.addAll(resultado.SUBCATEGORIAS)
            verifyAdapterIsEmpty()
            swipeRefresh.isRefreshing = false
        }, { it.stackTrace })
        jsonRequest.retryPolicy = DefaultRetryPolicy(30000, MAX_REQUESTS, 1.0f)
        requestQueue!!.add<JSONObject>(jsonRequest)
    }

    private fun verifyAdapterIsEmpty() {
        if(animesAdapter.itemCount == 0) {
            txtError.visibility = VISIBLE
            recyclerView.visibility = GONE
        } else {
            txtError.visibility = GONE
            recyclerView.visibility = VISIBLE
        }
    }

    companion object {
        private val ARG_SECTION_NUMBER = "section_number"
        private val TAG = "AnimesFragment"

        fun newInstance(sectionNumber: Int): AnimesFragment {
            val fragment = AnimesFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}