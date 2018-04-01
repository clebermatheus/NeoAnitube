package com.github.clebermatheus.neoanitube.anitube.views

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.clebermatheus.neoanitube.R
import com.github.clebermatheus.neoanitube.anitube.model.Subcategoria
import com.github.clebermatheus.neoanitube.anitube.model.Ultimos
import com.github.clebermatheus.neoanitube.anitube.viewmodels.AnimesViewAdapter
import com.github.clebermatheus.neoanitube.common.constants.API
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_lancamentos, container, false)
        animesAdapter = AnimesViewAdapter(ArrayList())
        this.requestQueueLancamentos(rootView.context)
        rootView.findViewById<RecyclerView>(R.id.lancView).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(rootView.context)
            adapter = animesAdapter
        }
        return rootView
    }

    private fun requestQueueLancamentos(context: Context) {
        if (requestQueue == null) requestQueue = Volley.newRequestQueue(context)

        val jsonRequest = JsonObjectRequest(API.SUBCATEGORIA+"anime", null, {
            val gson = Gson()
            val resultado: Subcategoria = gson.fromJson(it.toString(), Subcategoria::class.java)
            Log.d(TAG, resultado.toString())
            resultado.SUBCATEGORIAS.forEach { animesAdapter.add(it) }
        }, { it.stackTrace })
        jsonRequest.retryPolicy = DefaultRetryPolicy(30000, MAX_REQUESTS, 1.0f)
        requestQueue!!.add<JSONObject>(jsonRequest)
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