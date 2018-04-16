package com.github.clebermatheus.neoanitube.anitube.viewmodels

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
import com.google.gson.Gson

/**
 * Adapter do Recycler View da LancamentosFragment
 *
 * Created by clebermatheus on 27/03/18.
 */
class AnimesViewAdapter(
    private val animes: ArrayList<Anime>,
    private val activity: AppCompatActivity
) : RecyclerView.Adapter<AnimesViewAdapter.ViewHolder>() {

    class ViewHolder(
        v: View,
        activity: AppCompatActivity
    ) : RecyclerView.ViewHolder(v) {
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
                dialogFragment.show(activity.supportFragmentManager, dialogFragment.tag)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterView = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_animes, parent, false) as View
        return ViewHolder(adapterView, activity)
    }

    override fun getItemCount(): Int = animes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = animes[position].name
        holder.capa.setImageURI(API.CAPA+animes[position].capa)
        holder.anime = animes[position]
    }

    fun add(anime: Anime): AnimesViewAdapter {
        animes.add(anime)
        notifyDataSetChanged()

        return this
    }

    fun addAll(animes: List<Anime>): AnimesViewAdapter {
        animes.forEach { this.animes.add(it) }
        Log.i(TAG_DEBUG, animes.toString())
        notifyDataSetChanged()

        return this
    }

    fun clear(): AnimesViewAdapter {
        animes.clear()
        notifyDataSetChanged()

        return this
    }

    companion object {
        private const val TAG_DEBUG = "AnimesViewAdapter"
    }
}
