package com.github.clebermatheus.neoanitube.anitube.viewmodels

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.clebermatheus.neoanitube.R
import com.github.clebermatheus.neoanitube.anitube.constants.API
import com.github.clebermatheus.neoanitube.anitube.model.Episodio
import com.mikepenz.google_material_typeface_library.GoogleMaterial.Icon.gmd_hd
import com.mikepenz.iconics.IconicsDrawable

/**
 *
 * Classe para exibir o episódio
 *
 * Created by clebermatheus on 04/04/18.
 */
class EpisodioAdapter(
    val episodio: Episodio,
    val dataset: ArrayList<String>
): RecyclerView.Adapter<EpisodioAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_episodio, parent,
                false) as View
        return ViewHolder(episodio, v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = dataset[position]
        when(position) {
            0 -> {
                holder.tipo = Tipo.SD
            }
            1 -> {
                holder.tipo = Tipo.HD
                holder.image.setImageDrawable(IconicsDrawable(holder.v.context, gmd_hd))
            }
            else -> holder.tipo = Tipo.SD
        }
    }

    override fun getItemCount(): Int = dataset.size

    fun add(position: Int, item: String) {
        dataset.add(position, item)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        dataset.removeAt(position)
        notifyDataSetChanged()
    }

    class ViewHolder(
        val episodio: Episodio,
        val v: View
    ): RecyclerView.ViewHolder(v) {
        val text = v.findViewById<TextView>(R.id.txtItem)
        val image = v.findViewById<ImageView>(R.id.icon)
        var tipo = Tipo.SD

        init {
            v.setOnClickListener { v.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse
            (API.URL_EPISODIO+ this.tipo.label+"/"+episodio.vid+".mp4/playlist.m3u8"))) }
        }
    }

    enum class Tipo(val label: String){
        SD("sdmobile"),
        HD("hdmobile")
    }
}