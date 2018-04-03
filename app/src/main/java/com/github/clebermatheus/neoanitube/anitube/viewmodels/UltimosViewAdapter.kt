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
import com.github.clebermatheus.neoanitube.anitube.model.Episodio

/**
 * Adapter do Recycler View da UltimosFragment
 *
 * Created by clebermatheus on 27/03/18.
 */
class UltimosViewAdapter(private val episodios: ArrayList<Episodio>) :
        RecyclerView.Adapter<UltimosViewAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val capa: SimpleDraweeView = v.findViewById(R.id.capa)
        val text: TextView = v.findViewById(R.id.titulo)

        init {
            v.setOnClickListener({
                Log.d(TAG_DEBUG, "Element $adapterPosition clicked.")
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterView = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_episodios, parent, false) as View
        return ViewHolder(adapterView)
    }

    override fun getItemCount(): Int = episodios.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
        private val TAG_DEBUG = "UltimosViewAdapter"
    }
}
