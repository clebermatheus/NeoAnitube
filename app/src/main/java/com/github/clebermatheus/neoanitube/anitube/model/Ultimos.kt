package com.github.clebermatheus.neoanitube.anitube.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 *
 * Classe para armazenar o resultado obtido
 *
 * Created by clebermatheus on 31/03/18.
 */
data class Ultimos(
    @SerializedName("LANCAMENTOS")
    @Expose
    val LANCAMENTOS: ArrayList<Episodio>
)