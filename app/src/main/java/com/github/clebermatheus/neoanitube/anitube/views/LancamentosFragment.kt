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
import com.github.clebermatheus.neoanitube.anitube.model.Ultimos
import com.github.clebermatheus.neoanitube.anitube.viewmodels.EpisodiosViewAdapter
import com.github.clebermatheus.neoanitube.anitube.constants.API
import com.github.clebermatheus.neoanitube.common.constants.Utils.MAX_REQUESTS
import com.google.gson.Gson
import org.json.JSONObject

/**
 * Fragmento de Lançamentos
 *
 * Created by clebermatheus on 27/03/18.
 */
class LancamentosFragment : Fragment() {
    private var requestQueue: RequestQueue? = null
    private lateinit var episodiosAdapter: EpisodiosViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_lancamentos, container, false)
        episodiosAdapter = EpisodiosViewAdapter(ArrayList())
        this.requestQueueLancamentos(rootView.context)
        rootView.findViewById<RecyclerView>(R.id.lancView).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(rootView.context)
            adapter = episodiosAdapter
        }
        return rootView
    }

    private fun requestQueueLancamentos(context: Context) {
        if (requestQueue == null) requestQueue = Volley.newRequestQueue(context)

        val jsonRequest = JsonObjectRequest(API.LANCAMENTOS, null, {
            val gson = Gson()
            val resultado: Ultimos = gson.fromJson(it.toString(), Ultimos::class.java)
            Log.d(TAG, resultado.toString())
            resultado.LANCAMENTOS.forEach { episodiosAdapter.add(it) }
        }, { it.stackTrace })
        jsonRequest.retryPolicy = DefaultRetryPolicy(30000, MAX_REQUESTS, 1.0f)
        requestQueue!!.add<JSONObject>(jsonRequest)
    }

    companion object {
        private val ARG_SECTION_NUMBER = "section_number"
        private val TAG = "LancamentosFragment"

        fun newInstance(sectionNumber: Int): LancamentosFragment {
            val fragment = LancamentosFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}