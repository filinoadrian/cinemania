package com.far_sstrwnt.cinemania.shared.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.far_sstrwnt.cinemania.model.MediaEntity

@Database(entities = [MediaEntity::class], version = 1, exportSchema = false)
@TypeConverters(StringListConverter::class)
abstract class CinemaniaDatabase : RoomDatabase() {

    abstract fun mediaDao(): MediaDao
}