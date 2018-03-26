package com.github.clebermatheus.neoanitube.animes.model

/**
 * Objeto Descrição
 *
 * Created by clebermatheus on 25/03/18.
 */
data class Descricao(
    val chid: Int,
    val categoria_slug: String,
    val name: String,
    val titulo_alternativo: String,
    val total: String,
    val ano: Int,
    val generos: String,
    val descricao: String
    val total_arqiovps: String,
    val capa: String,
    val autor: String,
    val direcao: String,
    val estudio: String,
    val status_anime: String,
    val dia_lancamento: String,
    val slug: String,
    val total_videos: Int,
    val seo_desc: String
)