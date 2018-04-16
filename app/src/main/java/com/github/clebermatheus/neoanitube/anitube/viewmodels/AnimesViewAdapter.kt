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
import com.github.clebermatheus.neoanitube.anitube.model.Anime
import com.github.clebermatheus.neoanitube.anitube.views.BottomMenuAnimeFragment
import com.github.clebermatheus.neoanitube.common.MainActivity
import com.google.gson.Gson

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
        lateinit var anime: Anime

        init {
            v.setOnClickListener {
                Log.d(TAG_DEBUG, "Element $adapterPosition clicked.")
                val dialogFragment = BottomMenuAnimeFragment()
                val args = Bundle()
                args.putString("anime", Gson().toJson(anime))
                dialogFragment.arguments = args
                val main = v.context as MainActivity
                dialogFragment.show(main.supportFragmentManager, dialogFragment.tag)
            }
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
        holder.anime = animes[position]
    }

    fun add(anime: Anime){
        animes.add(anime)
        notifyDataSetChanged()
    }

    fun addAll(animes: ArrayList<Anime>) {
        animes.forEach { this.animes.add(it) }
        notifyDataSetChanged()
    }

    fun clear() {
        animes.clear()
        notifyDataSetChanged()
    }

    companion object {
        private val TAG_DEBUG = "EpisodiosViewAdapter"
    }
}
