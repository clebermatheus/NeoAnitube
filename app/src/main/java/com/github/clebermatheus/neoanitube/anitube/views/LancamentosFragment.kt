package com.github.clebermatheus.neoanitube.anitube.views

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request.Method.GET
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.github.clebermatheus.neoanitube.R
import com.github.clebermatheus.neoanitube.anitube.constants.API
import com.github.clebermatheus.neoanitube.anitube.model.Episodios
import com.github.clebermatheus.neoanitube.anitube.views.adapter.EpisodiosViewAdapter
import com.github.clebermatheus.neoanitube.common.constants.Utils.MAX_REQUESTS
import com.github.clebermatheus.neoanitube.common.models.GsonRequest

/**
 * Fragmento de Lançamentos
 *
 * Created by clebermatheus on 27/03/18.
 */
class LancamentosFragment : Fragment() {
    private var requestQueue: RequestQueue? = null
    private lateinit var episodiosAdapter: EpisodiosViewAdapter
    private lateinit var rvEpisodios: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var txtError: TextView
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_episodios, container, false)
        episodiosAdapter = EpisodiosViewAdapter(ArrayList())
        swipeRefresh = rootView.findViewById(R.id.swipeRefresh) as SwipeRefreshLayout
        txtError = rootView.findViewById(R.id.txt_episodio_not_found) as TextView
        this.requestQueueLancamentos(rootView.context)
        rvEpisodios = rootView.findViewById<RecyclerView>(R.id.rvEpisodios).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(rootView.context)
            adapter = episodiosAdapter
        }
        verifyAdapterIsEmpty()
        swipeRefresh.setOnRefreshListener { requestQueueLancamentos(rootView.context) }
        swipeRefresh.setColorSchemeResources(R.color.primaryColor, R.color.secondaryColor)
        return rootView
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_view -> {
            val ( episodios, tipoView) = episodiosAdapter
            item.icon = if(tipoView) {
                this.resources.getDrawable(R.drawable.ic_view_module_24dp, null)
            } else {
                this.resources.getDrawable(R.drawable.ic_view_list_24dp, null)
            }
            episodiosAdapter = EpisodiosViewAdapter(episodios, !tipoView)
            rvEpisodios.apply {
                layoutManager = if(!tipoView) {
                    GridLayoutManager(rootView.context, 4)
                } else { LinearLayoutManager(rootView.context) }
                adapter = episodiosAdapter
                setHasFixedSize(true)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun requestQueueLancamentos(context: Context) {
        if (requestQueue == null) requestQueue = Volley.newRequestQueue(context)

        val gsonRequest = GsonRequest<Episodios>(GET, API.LANCAMENTOS, Episodios::class.java,
                null, Response.Listener {
            Log.d(TAG, it.toString())
            episodiosAdapter.clear().addAll(it.LANCAMENTOS)
            verifyAdapterIsEmpty()
            swipeRefresh.isRefreshing = false
        }, Response.ErrorListener { it.stackTrace })
        gsonRequest.retryPolicy = DefaultRetryPolicy(30000, MAX_REQUESTS, 1.0f)
        requestQueue!!.add(gsonRequest)
    }

    private fun verifyAdapterIsEmpty() {
        if(episodiosAdapter.itemCount == 0) {
            txtError.visibility = VISIBLE
            rvEpisodios.visibility = GONE
        } else {
            txtError.visibility = GONE
            rvEpisodios.visibility = VISIBLE
        }
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val TAG = "LancamentosFragment"

        fun newInstance(sectionNumber: Int): LancamentosFragment {
            val fragment = LancamentosFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}