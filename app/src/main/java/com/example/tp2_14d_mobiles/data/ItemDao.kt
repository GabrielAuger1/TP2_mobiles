package com.example.tp2_14d_mobiles.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tp2_14d_mobiles.model.Item
@Dao

// Interface qui définit les opérations d'accès aux données pour l'entité Item, contient
// des méthodes pour insérer, mettre à jour, supprimer et interroger des éléments dans la table "items".
interface ItemDao {

    @Insert
    fun insertItem(item: Item)

    @Insert
    fun insertItemReturnId(item: Item): Long

    @Insert
    fun insertItems(items: List<Item>): List<Long>

    @Update
    fun updateItem(item: Item)

    @Delete
    fun deleteItem(item: Item)

    @Query("SELECT * FROM items WHERE id = :id")
    fun getItemById(id: Int): LiveData<Item>

    @Query("SELECT * FROM items")
    fun getAllItems(): MutableList<Item>

    @Query("DELETE FROM items")
    fun deleteAllItems()

    companion object
}