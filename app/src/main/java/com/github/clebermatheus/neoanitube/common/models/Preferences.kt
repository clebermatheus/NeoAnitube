package com.github.clebermatheus.neoanitube.common.models

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.github.clebermatheus.neoanitube.anitube.model.Subcategoria
import com.google.gson.Gson

/**
 *
 * Interface que trata os dados armazenados no SharedPreferences
 *
 * Created by clebermatheus on 16/04/18.
 */
class Preferences(context: Context) {
    private var preferences: SharedPreferences
    private var editor: SharedPreferences.Editor
    private val gson = Gson()

    init {
        preferences = context.getSharedPreferences(PREFERENCES, Activity.MODE_PRIVATE)
        editor = preferences.edit()
        editor.apply()
    }

    // Armazena strings
    fun getString(key: String): String = preferences.getString(key, "")
    fun putString(key: String, value: String) = editor.putString(key, value).apply()

    // Armazena Subcategorias
    fun getSubcategoria(key: String): Subcategoria {
        return gson.fromJson(preferences.getString(key, "{\"SUBCATEGORIAS\": []}"),
    Subcategoria::class.java)
    }
    fun putSubcategoria(key: String, value: Subcategoria) = editor.putString(key, gson.toJson
    (value)).apply()

    companion object {
        val PREFERENCES = Preferences::class.java.simpleName
    }
}