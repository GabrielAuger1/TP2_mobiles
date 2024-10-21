package com.example.tp2_14d_mobiles.data
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tp2_14d_mobiles.model.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Classe abstraite qui représente la bd pour l'entité Item.
// Contient la définition de la table et donne accès au DAO.
@Database(entities = [Item::class], version = 1)
abstract class ItemDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        var INSTANCE: ItemDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): ItemDatabase {
            if (INSTANCE == null) {
                INSTANCE = databaseBuilder(
                    context.applicationContext,
                    ItemDatabase::class.java,
                    "item_database"
                )
                    .build()
            }
            return INSTANCE!!
        }
    }
}