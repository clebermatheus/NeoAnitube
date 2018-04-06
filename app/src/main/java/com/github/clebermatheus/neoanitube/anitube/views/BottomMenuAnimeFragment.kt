package com.github.clebermatheus.neoanitube.anitube.views

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.facebook.drawee.view.SimpleDraweeView
import com.github.clebermatheus.neoanitube.R
import com.github.clebermatheus.neoanitube.anitube.constants.API
import com.github.clebermatheus.neoanitube.anitube.model.Anime
import com.github.clebermatheus.neoanitube.anitube.model.Subcategoria
import com.github.clebermatheus.neoanitube.common.constants.Utils.MAX_REQUESTS
import com.google.gson.Gson
import org.json.JSONObject

/**
 *
 * Classe fragmento que exibe detalhes do episodio
 *
 * Created by clebermatheus on 04/04/18.
 */
class BottomMenuAnimeFragment: BottomSheetDialogFragment() {
    private var requestQueue: RequestQueue? = null
    lateinit var anime: Anime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            anime = Gson().fromJson(arguments!!.getString("anime"), Anime::class.java)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        val v = View.inflate(context, R.layout.bottom_menu_anime, null) as View
        val capa = v.findViewById<SimpleDraweeView>(R.id.capa)
        capa.setImageURI(API.CAPA+anime.capa)
        val nome = v.findViewById<TextView>(R.id.nome)
        nome.text = anime.name
        val botaoEpisodios = v.findViewById<Button>(R.id.bottom_menu_botao_episodios)
        botaoEpisodios.setOnClickListener {
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

        val jsonRequest = JsonObjectRequest(API.DESCRICAO+anime.chid, null, {
            val gson = Gson()
            val resultado: Subcategoria = gson.fromJson(it.toString(), Subcategoria::class.java)
            Log.d(TAG, resultado.toString())

            val descricao = v.findViewById<TextView>(R.id.descricao)
            val generos = v.findViewById<TextView>(R.id.generos)
            resultado.SUBCATEGORIAS_DESCRICAO.forEach {
                descricao.text = Html.fromHtml(it.descricao)
                generos.text = Html.fromHtml(it.generos)
            }
        }, { it.stackTrace })
        jsonRequest.retryPolicy = DefaultRetryPolicy(30000, MAX_REQUESTS, 1.0f)
        requestQueue!!.add<JSONObject>(jsonRequest)
    }

    inner class BehaviorInternal: BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when(newState){
                BottomSheetBehavior.STATE_HIDDEN -> dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    companion object {
        const val TAG = "BottomMenuAnimeFragment"
    }
}