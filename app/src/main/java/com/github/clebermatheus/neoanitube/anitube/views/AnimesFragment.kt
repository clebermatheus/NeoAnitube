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
import com.android.volley.Response.ErrorListener
import com.android.volley.toolbox.Volley
import com.github.clebermatheus.neoanitube.R
import com.github.clebermatheus.neoanitube.anitube.constants.API
import com.github.clebermatheus.neoanitube.anitube.model.Subcategoria
import com.github.clebermatheus.neoanitube.anitube.viewmodels.AnimesViewAdapter
import com.github.clebermatheus.neoanitube.common.constants.Utils.MAX_REQUESTS
import com.github.clebermatheus.neoanitube.common.constants.Utils.PREF_ANIMES
import com.github.clebermatheus.neoanitube.common.models.GsonRequest
import com.github.clebermatheus.neoanitube.common.models.Preferences
import com.github.clebermatheus.neoanitube.common.views.MainActivity
import java.lang.reflect.Type

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
    private lateinit var preferences: Preferences
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_animes, container, false)
        preferences = Preferences(rootView.context)
        val subcategoria = preferences.getSubcategoria(PREF_ANIMES)
        animesAdapter = AnimesViewAdapter(subcategoria.SUBCATEGORIAS, rootView.context as MainActivity)
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
        swipeRefresh.setColorSchemeResources(R.color.primaryColor, R.color.secondaryColor)
        return rootView
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_view -> {
            val ( animes, _, tipoView) = animesAdapter
            item.icon = if(tipoView) {
                this.resources.getDrawable(R.drawable.ic_view_module_24dp, null)
            } else {
                this.resources.getDrawable(R.drawable.ic_view_list_24dp, null)
            }
            animesAdapter = AnimesViewAdapter(animes, rootView.context as MainActivity, !tipoView)
            recyclerView.apply {
                layoutManager = if(!tipoView) {
                    GridLayoutManager(rootView.context, 4)
                } else { LinearLayoutManager(rootView.context) }
                adapter = animesAdapter
                setHasFixedSize(true)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun requestQueueAnimes(context: Context) {
        if (requestQueue == null) requestQueue = Volley.newRequestQueue(context)

        val gsonRequest = GsonRequest<Subcategoria>(GET, API.SUBCATEGORIA+"anime",
                Subcategoria::class.java, null, Response.Listener {
            Log.d(TAG, it.toString())
            preferences.putSubcategoria(PREF_ANIMES, it)
            animesAdapter.clear().addAll(it.SUBCATEGORIAS)
            verifyAdapterIsEmpty()
            swipeRefresh.isRefreshing = false
        }, ErrorListener { it.stackTrace })
        gsonRequest.retryPolicy = DefaultRetryPolicy(30000, MAX_REQUESTS, 1.0f)
        requestQueue!!.add(gsonRequest)
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
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val TAG = "AnimesFragment"

        fun newInstance(sectionNumber: Int): AnimesFragment {
            val fragment = AnimesFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}