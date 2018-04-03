package com.github.clebermatheus.neoanitube.anitube.viewmodels

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.github.clebermatheus.neoanitube.R
import com.github.clebermatheus.neoanitube.anitube.constants.API
import com.github.clebermatheus.neoanitube.anitube.model.Anime

/**
 * Adapter do Recycler View da LancamentosFragment
 *
 * Created by clebermatheus on 27/03/18.
 */
class AnimesViewAdapter(private val animes: ArrayList<Anime>) :
        RecyclerView.Adapter<AnimesViewAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val capa: SimpleDraweeView = v.findViewById(R.id.capaAnime)
        val text: TextView = v.findViewById(R.id.titulo)

        init {
            v.setOnClickListener({
                Log.d(TAG_DEBUG, "Element $adapterPosition clicked.")
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterView = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_animes, parent, false) as View
        return ViewHolder(adapterView)
    }

    override fun getItemCount(): Int = animes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = animes[position].name
        holder.capa.setImageURI(API.CAPA+animes[position].capa)
    }

    fun add(anime: Anime){
        animes.add(anime)
        notifyDataSetChanged()
    }

    fun remove(anime: Anime) {
        animes.remove(anime)
        notifyDataSetChanged()
    }

    companion object {
        private val TAG_DEBUG = "LancamentosViewAdapter"
    }
}
