package com.github.clebermatheus.neoanitube.anitube.views

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.*
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request.Method.GET
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.facebook.drawee.view.SimpleDraweeView
import com.github.clebermatheus.neoanitube.R
import com.github.clebermatheus.neoanitube.anitube.constants.API
import com.github.clebermatheus.neoanitube.anitube.model.Anime
import com.github.clebermatheus.neoanitube.anitube.model.Subcategoria
import com.github.clebermatheus.neoanitube.common.constants.Utils.MAX_REQUESTS
import com.github.clebermatheus.neoanitube.common.models.GsonRequest
import com.google.gson.Gson

/**
 *
 * Classe fragmento que exibe detalhes do episodio
 *
 * Created by clebermatheus on 04/04/18.
 */
class BottomMenuAnimeFragment: BottomSheetDialogFragment() {
    private var requestQueue: RequestQueue? = null
    private lateinit var anime: Anime
    private lateinit var gson: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gson = Gson()
        if(arguments != null){
            anime = gson.fromJson(arguments!!.getString("anime"), Anime::class.java)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        val v = View.inflate(context, R.layout.bottom_menu_anime, null) as View
        val capa = v.findViewById<SimpleDraweeView>(R.id.capa)
        val collapseToolbar = v.findViewById<CollapsingToolbarLayout>(R.id.ctl1)
        val fab = v.findViewById<FloatingActionButton>(R.id.fab)

        capa.setImageURI(API.CAPA+anime.capa)
        collapseToolbar.title = anime.name
        fab.setOnClickListener {
            val intent = Intent(v.context, EpisodiosActivity::class.java)
            intent.putExtra("anime", Gson().toJson(anime))
            v.context.startActivity(intent)
        }
        dialog!!.setContentView(v)

        requestQueueDescricao(v)
        val params = (v.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior: CoordinatorLayout.Behavior<View>? = params.behavior
        if(behavior != null && behavior is BottomSheetBehavior){
            behavior.setBottomSheetCallback(BehaviorInternal())
        }
    }

    private fun requestQueueDescricao(v: View) {
        if (requestQueue == null) requestQueue = Volley.newRequestQueue(context)

        val gsonRequest = GsonRequest<Subcategoria>(GET, API.DESCRICAO+anime.chid, Subcategoria::class.java,
                null, Response.Listener {
            Log.d(TAG, it.toString())
            val descricao = v.findViewById<TextView>(R.id.descricao)
            val generos = v.findViewById<TextView>(R.id.generos)
            val autor = v.findViewById<TextView>(R.id.autor)
            val estudio = v.findViewById<TextView>(R.id.estudio)
            val anoLancamento = v.findViewById<TextView>(R.id.ano_lancamento)
            val status = v.findViewById<TextView>(R.id.status)
            val direcao = v.findViewById<TextView>(R.id.direcao)

            it.SUBCATEGORIAS_DESCRICAO.forEach {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    descricao.text = Html.fromHtml(it.descricao, Html.FROM_HTML_MODE_LEGACY)
                } else { descricao.text = Html.fromHtml(it.descricao) }
                generos.text = it.generos
                autor.text = it.autor
                estudio.text = it.estudio
                anoLancamento.text = String.format("%d", it.ano)
                status.text = "${it.total} - ${it.status_anime}"
                direcao.text = it.direcao
            }
        }, Response.ErrorListener { it.stackTrace })
        gsonRequest.retryPolicy = DefaultRetryPolicy(30000, MAX_REQUESTS, 1.0f)
        requestQueue!!.add(gsonRequest)
    }

    inner class BehaviorInternal: BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) = when(newState) {
            BottomSheetBehavior.STATE_HIDDEN -> dismiss()
            else -> {}
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    companion object {
        const val TAG = "BottomMenuAnimeFragment"
    }
}