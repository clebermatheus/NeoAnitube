package com.github.clebermatheus.neoanitube.anitube.views.adapter

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
import com.github.clebermatheus.neoanitube.anitube.model.Episodio
import com.github.clebermatheus.neoanitube.anitube.views.BottomMenuEpisodioFragment
import com.google.gson.Gson

/**
 * Adapter para exibir os Episódios
 *
 * Created by clebermatheus on 27/03/18.
 */
class EpisodiosViewAdapter(
        private val episodios: ArrayList<Episodio>,
        private val tipoView: Boolean = false
) : RecyclerView.Adapter<EpisodiosViewAdapter.ViewHolder>() {

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
                val main = v.context as AppCompatActivity
                dialogFragment.show(main.supportFragmentManager, dialogFragment.tag)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val adapterView = if(tipoView) {
            layoutInflater.inflate(R.layout.adapter_grid, parent, false)
        } else {
            layoutInflater.inflate(R.layout.adapter_list, parent, false)
        }
        return ViewHolder(adapterView)
    }

    override fun getItemCount(): Int = episodios.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.episodio = episodios[position]
        holder.text.text = episodios[position].title
        holder.capa.setImageURI(API.CAPA_EPISODIOS+episodios[position].imagem+"/1.jpg")
    }

    fun add(episodio: Episodio): EpisodiosViewAdapter {
        episodios.add(episodio)
        notifyDataSetChanged()
        return this
    }

    fun addAll(episodios: ArrayList<Episodio>): EpisodiosViewAdapter {
        episodios.forEach { this.episodios.add(it) }
        notifyDataSetChanged()
        return this
    }

    fun clear(): EpisodiosViewAdapter {
        episodios.clear()
        notifyDataSetChanged()
        return this
    }

    fun remove(episodio: Episodio): EpisodiosViewAdapter {
        episodios.remove(episodio)
        notifyDataSetChanged()
        return this
    }

    operator fun component1(): ArrayList<Episodio> = this.episodios
    operator fun component2(): Boolean = this.tipoView

    companion object {
        private val TAG_DEBUG = "EpisodiosViewAdapter"
    }
}
