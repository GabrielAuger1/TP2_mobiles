package com.example.tp2_14d_mobiles.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


// Définition d'une entité de la base de données qui représente la table "items".

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "nom")
    val nom: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "prix")
    val prix: Double,

    @ColumnInfo(name = "categorie")
    val categorie: String
) {
    constructor(id: Int, nom: String, description: String) : this(id, nom, description, 0.0, "Autre")
}