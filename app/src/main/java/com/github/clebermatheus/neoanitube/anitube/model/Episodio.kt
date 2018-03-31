package com.github.clebermatheus.neoanitube.anitube.model

/**
 * Objeto Episódio
 *
 * Created by clebermatheus on 25/03/18.
 */

data class Episodio(
        val vid: Int = 0,
        val title: String = "",
        val numero: String = "",
        val imagem: String = "",
        val viewnumber: Double = 0.0,
        val addtime: String = ""
)
