package com.github.clebermatheus.neoanitube.anitube.views

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.github.clebermatheus.neoanitube.R
import com.github.clebermatheus.neoanitube.anitube.constants.API
import com.github.clebermatheus.neoanitube.anitube.model.Episodio
import com.github.clebermatheus.neoanitube.anitube.views.adapter.EpisodioAdapter
import com.google.gson.Gson

/**
 *
 * Classe fragmento que exibe detalhes do episodio
 *
 * Created by clebermatheus on 04/04/18.
 */
class BottomMenuEpisodioFragment: BottomSheetDialogFragment() {
    lateinit var episodio: Episodio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            episodio = Gson().fromJson(arguments!!.getString("episodio"), Episodio::class.java)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        val v = View.inflate(context, R.layout.bottom_menu_episodio, null) as View
        val recyclerView = v.findViewById<RecyclerView>(R.id.listBotoes)
        val rvLayoutManager = LinearLayoutManager(v.context)
        val values = ArrayList<String>()
        val capa = v.findViewById<SimpleDraweeView>(R.id.capa)
        capa.setImageURI(API.CAPA_EPISODIOS+episodio.imagem+"/1.jpg")
        val nome = v.findViewById<TextView>(R.id.nome)
        nome.text = episodio.title
        dialog!!.setContentView(v)

        val params = (v.parent as View).layoutParams as CoordinatorLayout.LayoutParams

        values.add("Assistir em SD")
        values.add("Assistir em HD")

        val adapter = EpisodioAdapter(episodio, values)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = rvLayoutManager
            this.adapter = adapter
        }

        val behavior: CoordinatorLayout.Behavior<View>? = params.behavior
        if(behavior != null && behavior is BottomSheetBehavior){
            behavior.setBottomSheetCallback(BehaviorInternal())
        }
    }

    inner class BehaviorInternal: BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when(newState){
                BottomSheetBehavior.STATE_HIDDEN -> dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }
}