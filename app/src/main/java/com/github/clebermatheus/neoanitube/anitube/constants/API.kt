package com.github.clebermatheus.neoanitube.anitube.constants

/**
 * Endereços da AnitubeBR API
 *
 * Created by clebermatheus on 25/03/18.
 */
object API {
    private const val URL = "http://anitubebr.biz/api/"
    private const val URL_CDN = "https://cdn.anitube.info/"
    const val URL_EPISODIO = "http://fr40.anitube.info/"
    const val LANCAMENTOS = URL +"lancamentos.php"
    const val DESCRICAO = URL +"descricao/"
    const val EPISODIOS = URL +"episodios/"
    const val SUBCATEGORIA = URL +"subcategorias/"
    const val ULTIMOS = URL +"ultimos.php"

    const val CAPA = URL_CDN+"media/categories/video/"
    const val CAPA_EPISODIOS = URL_CDN+"media/videos/tmb/"
}
