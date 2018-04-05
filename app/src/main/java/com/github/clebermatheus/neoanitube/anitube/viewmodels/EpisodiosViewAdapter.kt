package com.github.clebermatheus.neoanitube.anitube.viewmodels

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.github.clebermatheus.neoanitube.R
import com.github.clebermatheus.neoanitube.anitube.constants.API
import com.github.clebermatheus.neoanitube.anitube.model.Episodio
import com.github.clebermatheus.neoanitube.anitube.views.BottomMenuEpisodioFragment
import com.github.clebermatheus.neoanitube.common.MainActivity
import com.google.gson.Gson

/**
 * Adapter do Recycler View da LancamentosFragment
 *
 * Created by clebermatheus on 27/03/18.
 */
class EpisodiosViewAdapter(private val episodios: ArrayList<Episodio>) :
        RecyclerView.Adapter<EpisodiosViewAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val capa: SimpleDraweeView = v.findViewById(R.id.capa)
        val text: TextView = v.findViewById(R.id.titulo)
        lateinit var episodio: Episodio

        init {
            v.setOnClickListener {
                Log.d(TAG_DEBUG, "Element $adapterPosition clicked.")
                val dialogFragment = BottomMenuEpisodioFragment()
                val args = Bundle()
                args.putString("episodio", Gson().toJson(episodio))
                dialogFragment.arguments = args
                val main = v.context as MainActivity
                dialogFragment.show(main.supportFragmentManager, dialogFragment.tag)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterView = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_episodios, parent, false) as View
        return ViewHolder(adapterView)
    }

    override fun getItemCount(): Int = episodios.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.episodio = episodios[position]
        holder.text.text = episodios[position].title
        holder.capa.setImageURI(API.CAPA_EPISODIOS+episodios[position].imagem+"/1.jpg")
    }

    fun add(episodio: Episodio){
        episodios.add(episodio)
        notifyDataSetChanged()
    }

    fun remove(episodio: Episodio) {
        episodios.remove(episodio)
        notifyDataSetChanged()
    }

    companion object {
        private val TAG_DEBUG = "EpisodiosViewAdapter"
    }
}
