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
import com.github.clebermatheus.neoanitube.anitube.constants.API
import com.github.clebermatheus.neoanitube.anitube.model.Ultimos
import com.github.clebermatheus.neoanitube.anitube.viewmodels.EpisodiosViewAdapter
import com.github.clebermatheus.neoanitube.common.constants.Utils.MAX_REQUESTS
import com.google.gson.Gson
import org.json.JSONObject

/**
 * Fragmento do Últimos lançamentos
 *
 * Created by clebermatheus on 27/03/18.
 */
class UltimosFragment : Fragment() {
    private var requestQueue: RequestQueue? = null
    private lateinit var ultimosAdapter: EpisodiosViewAdapter
    private lateinit var rvEpisodios: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var txtError: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_episodios, container, false)
        ultimosAdapter = EpisodiosViewAdapter(ArrayList())
        swipeRefresh = rootView.findViewById(R.id.swipeRefresh) as SwipeRefreshLayout
        txtError = rootView.findViewById(R.id.txt_episodio_not_found) as TextView
        this.requestQueueUltimos(rootView.context)
        rvEpisodios = rootView.findViewById<RecyclerView>(R.id.rvEpisodios).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(rootView.context)
            adapter = ultimosAdapter
        }
        verifyAdapterIsEmpty()
        swipeRefresh.setOnRefreshListener { requestQueueUltimos(rootView.context) }
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_dark)
        return rootView
    }

    private fun requestQueueUltimos(context: Context) {
        if (requestQueue == null) requestQueue = Volley.newRequestQueue(context)

        val jsonRequest = JsonObjectRequest(API.ULTIMOS, null, {
            val gson = Gson()
            val resultado: Ultimos = gson.fromJson(it.toString(), Ultimos::class.java)
            Log.d(TAG, resultado.toString())
            ultimosAdapter.clear()
            ultimosAdapter.addAll(resultado.LANCAMENTOS)
            verifyAdapterIsEmpty()
            swipeRefresh.isRefreshing = false
        }, { it.stackTrace })
        jsonRequest.retryPolicy = DefaultRetryPolicy(30000, MAX_REQUESTS, 1.0f)
        requestQueue!!.add<JSONObject>(jsonRequest)
    }

    private fun verifyAdapterIsEmpty() {
        if(ultimosAdapter.itemCount == 0) {
            txtError.visibility = VISIBLE
            rvEpisodios.visibility = GONE
        } else {
            txtError.visibility = GONE
            rvEpisodios.visibility = VISIBLE
        }
    }

    companion object {
        private val ARG_SECTION_NUMBER = "section_number"
        private val TAG = "UltimosFragment"

        fun newInstance(sectionNumber: Int): UltimosFragment {
            val fragment = UltimosFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}