package com.github.clebermatheus.neoanitube.animes.model

/**
 * Objeto Episódio
 *
 * Created by clebermatheus on 25/03/18.
 */

data class Episodio(
    val vid: Int,
    val title: String,
    val numero: String,
    val imagem: String,
    val viewnumber: Float,
    val addtime: Float
)
